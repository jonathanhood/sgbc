package com.jhood.sgbc.cpu

import com.jhood.sgbc.cpu.instructions.{ImplementedInstruction, InstructionTable, NotImplementedInstruction}
import com.jhood.sgbc.memory.MemoryController

sealed abstract class Operand16 { def name: String }
object ZeroPage extends Operand16 { def name: String = "a8"}
object Immediate16 extends Operand16 { def name: String = "d16" }
sealed case class Register16(name: String, offset: Int) extends Operand16
object AF  extends Register16("AF",0)
object BC  extends Register16("BC",1)
object DE  extends Register16("DE",2)
object HL  extends Register16("HL",3)
object SP  extends Register16("SP", 4)
object PC  extends Register16("PC", 5)
object HLI extends Register16("HL+", 3)
object HLD extends Register16("HL-", 3)


sealed abstract class Operand8 { def name: String }
object Immediate8 extends Operand8 { def name: String = "d8" }
case class Memory8(addrSource: Operand16) extends Operand8 {
  def name: String = s"(${addrSource.name})"
}
sealed case class Register8(name: String, partOf: Register16, offset: Int) extends Operand8
object F extends Register8("F",AF,0)
object A extends Register8("A",AF,8)
object C extends Register8("C",BC,0)
object B extends Register8("B",BC,8)
object E extends Register8("E",DE,0)
object D extends Register8("D",DE,8)
object L extends Register8("L",HL,0)
object H extends Register8("H",HL,8)

class CPU(memController: MemoryController) {
  private var registers: Array[Short] = Array.fill(6)(0)
  Registers.write(A,0x01.toByte)    // Identify ourselves as DMB

  def tick: Int = {
    val addr = Registers.read(PC)
    val opcode = memController.fetch(Registers.read(PC))& 0x0FF
    try {
      InstructionTable.instructions(opcode) match {
        case inst : ImplementedInstruction =>
          //println(s"0x${addr.toHexString} ${inst.name}")
          //(1 until inst.width).foreach {i => println(s" $i - ${(memController.fetch((Registers.read(PC) + i).toShort) & 0x0FF).toHexString}")}
          inst.execute(this)
          inst.cycles
        case NotImplementedInstruction =>
          throw new Exception(s"Instruction is not implemented.")
      }
    } catch {
      case ex: Exception =>
        throw new Exception(s"Error encountered while processing opcode 0x${opcode.toHexString} at address 0x${addr.toHexString}", ex)
    }
  }

  def incrementPC(inst: ImplementedInstruction): Unit =
    Registers.write(PC, (Registers.read(PC) + inst.width).toShort)

  def read(idx: Operand8): Byte =
    idx match {
      case m : Memory8 => Memory.read(m)
      case r : Register8 => Registers.read(r)
      case Immediate8 => memController.fetch((Registers.read(PC) + 1).toShort)
    }

  def write(idx: Operand8, value: Byte): Unit =
    idx match {
      case m : Memory8 => Memory.write(m, value)
      case r : Register8 => Registers.write(r, value)
    }

  def read(idx: Operand16): Short =
    idx match {
      case Immediate16 =>
        val lower = memController.fetch((Registers.read(PC) + 1).toShort)
        val upper = memController.fetch((Registers.read(PC) + 2).toShort)
        ((upper << 8) + lower).toShort
      case ZeroPage =>
        val lower = memController.fetch((Registers.read(PC) + 1).toShort)
        (0xFF00 + lower).toShort
      case r: Register16 =>
        Registers.read(r)
    }


  object Memory {
    def read(idx: Memory8): Byte = {
      val addr = CPU.this.read(idx.addrSource)
      memController.fetch(addr)
    }

    def write(idx: Memory8, value: Byte): Unit = {
      val addr = CPU.this.read(idx.addrSource)
      memController.write(addr, value)
    }
  }

  object Registers {
    def read(idx: Register8): Byte =
      (registers(idx.partOf.offset) >>> idx.offset).toByte

    def write(idx: Register8, value: Byte): Unit = {
      val mask = ~(0xFFl << idx.offset)
      val updated = (registers(idx.partOf.offset) & mask) | (value.toShort << idx.offset)
      registers(idx.partOf.offset) = updated.toShort
    }

    def read(idx: Register16): Short =
      registers(idx.offset)

    def write(idx: Register16, value: Short): Unit =
      registers(idx.offset) = value

    def increment(idx: Register16): Unit =
      registers(idx.offset) = (registers(idx.offset) + 1).toShort

    def decrement(idx: Register16): Unit =
      registers(idx.offset) = (registers(idx.offset) - 1).toShort

    override def toString: String =
      List(A,F,B,C,D,E,H,L).map(reg => s"${reg.name}=${(read(reg) & 0x0FF).toHexString}").reduce(_ + " " + _)
  }

  object Flags {
    case class FlagImpl(name: String, offset: Byte) {
      def set(value: Boolean): Unit =
        if(value) {
          val mask = 1l << offset
          write(F, (read(F) | mask).toByte)
        } else {
          val mask = ~(1l << offset)
          write(F, (read(F) & mask).toByte)
        }

      def isSet: Boolean =
        ((read(F) >>> offset) & 0x01) == 0x01

      def get: Int =
        if(isSet) 1 else 0
    }

    object Z extends FlagImpl("Z",7)
    object N extends FlagImpl("N",6)
    object H extends FlagImpl("H",5)
    object C extends FlagImpl("C",4)

    override def toString: String =
      List(Z,N,H,C).map(flag => s"${flag.name}=${flag.get}").reduce(_ + " " + _)
  }

  object ALU {
    def Oper8(left: Byte, right: Byte, operation: (Byte, Byte) => Int): Byte = {
      val wideResult = operation(left,right)
      val result = wideResult.toByte
      val flags = left ^ right ^ wideResult
      Flags.Z.set(result == 0)
      Flags.H.set((flags ^ 0x10) != 0)
      Flags.C.set((flags ^ 0x100) != 0)
      result
    }
  }
}
