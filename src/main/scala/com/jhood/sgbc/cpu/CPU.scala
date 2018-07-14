package com.jhood.sgbc.cpu

import com.jhood.sgbc.cpu.instructions.{ImplementedInstruction, InstructionTable, NotImplementedInstruction}
import com.jhood.sgbc.interrupts.InterruptController
import com.jhood.sgbc.memory.{MappedMemoryController}

sealed abstract class Operand16 { def name: String }
case class ZeroPage(lower: Operand8) extends Operand16 { def name: String = lower.name}
object Immediate16 extends Operand16 { def name: String = "d16" }
object PaddedImmediate8 extends Operand16 { def name: String = "d8" }
case class Memory16(addrSource: Operand16) extends Operand16 {
  def name: String = s"(${addrSource.name})"
}
sealed case class Register16(name: String, offset: Int) extends Operand16
object AF  extends Register16("AF",0)
object BC  extends Register16("BC",1)
object DE  extends Register16("DE",2)
object HL  extends Register16("HL",3)
object SP  extends Register16("SP", 4)
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

case class CPU(interruptController: InterruptController, memController: MappedMemoryController) {
  private object PC extends Register16("PC", 5)

  private var registers: Array[Short] = Array.empty
  reset

  def reset: Unit = {
    registers = Array.fill(6)(0)
    Registers.write(AF,0x01B0.toShort) // Identify ourselves as DMG, Set ZHC flags
    Registers.write(PC,0x0100.toShort) // Skip the "Boot ROM" and jump straight to cart initialization
    Registers.write(SP,0xFFFE.toShort) // Set the startup stack pointer to end of memory
    Registers.write(BC,0x0013.toShort) // Set BC as left over by the boot rom
    Registers.write(DE,0x00D8.toShort) // Set DE as left over by the boot rom
    Registers.write(HL,0x014D.toShort) // Set HL as left over by the boot rom
  }

  def tick: ImplementedInstruction = {
    val addr = getAndIncrementPC
    val opcode = memController.fetch(addr) & 0x0FF
    try {
      InstructionTable.instructions(opcode) match {
        case inst: ImplementedInstruction =>
          //println(s"${addr.toHexString} ${inst.name}")
          inst.execute(this)
          inst
        case NotImplementedInstruction =>
          throw new Exception(s"Instruction is not implemented.")
      }
    } catch {
      case ex: Exception =>
        throw new Exception(s"Error encountered while processing opcode 0x${opcode.toHexString} at address 0x${addr.toHexString}", ex)
    }
  }

  def getAndIncrementPC: Short = {
    val pc = Registers.read(PC)
    Registers.increment(PC)
    pc
  }

  def writePC(value: Short): Unit =
    Registers.write(PC, value)

  def read(idx: Operand8): Byte =
    idx match {
      case m : Memory8    => Memory.read(m)
      case r : Register8  => Registers.read(r)
      case Immediate8     => memController.fetch(getAndIncrementPC)
    }

  def write(idx: Operand8, value: Byte): Unit =
    idx match {
      case m : Memory8 => Memory.write(m, value)
      case r : Register8 => Registers.write(r, value)
    }

  def write(idx: Operand16, value: Short): Unit =
    idx match {
      case r : Register16 => Registers.write(r, value)
      case m : Memory16   => Memory.write(m, value)
    }

  def read(idx: Operand16): Short =
    idx match {
      case Immediate16 =>
        val lower = memController.fetch(getAndIncrementPC) & 0x0FF
        //println( s" imm 0x${lower.toHexString}")
        val upper = memController.fetch(getAndIncrementPC) & 0x0FF
        //println( s" imm 0x${upper.toHexString}")
        ((upper << 8) + lower).toShort
      case PaddedImmediate8 =>
        val lower = memController.fetch(getAndIncrementPC).toShort
        lower.toShort
      case ZeroPage(operand) =>
        val lower = read(operand) & 0x0FF
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

    def write(idx: Memory16, value: Short): Unit = {
      val lowerAddr = CPU.this.read(idx.addrSource)
      val upperAddr = (lowerAddr + 1).toShort
      val lower = (value & 0x0FF).toByte
      val upper = (value & 0x0FF00) >>> 8
      memController.write(lowerAddr, lower)
      memController.write(upperAddr, upper.toByte)
    }
  }

  object Registers {
    def read(idx: Register8): Byte =
      (registers(idx.partOf.offset) >>> idx.offset).toByte

    def write(idx: Register8, value: Byte): Unit = {
      val realValue = if(idx == F) (value & 0xF0).toByte else value
      val lower = if(idx.offset == 0) realValue else registers(idx.partOf.offset) & 0x0FF
      val upper = if(idx.offset == 8) realValue << 8 else registers(idx.partOf.offset) & 0x0FF00
      registers(idx.partOf.offset) = ((upper & 0x0FF00) | (lower & 0x0FF)).toShort
    }

    def read(idx: Register16): Short =
      registers(idx.offset)

    def write(idx: Register16, value: Short): Unit =
      registers(idx.offset) = if(idx == AF) (value & 0xFFF0).toShort else value

    def increment(idx: Register16): Unit =
      registers(idx.offset) = (registers(idx.offset) + 1).toShort

    def decrement(idx: Register16): Unit =
      registers(idx.offset) = (registers(idx.offset) - 1).toShort

    override def toString: String =
      List(SP,PC).map(reg => s"${reg.name}=${(read(reg) & 0x0FFFF).toHexString}").reduce(_ + " " + _) +
        " " +
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
    def Oper8(left: Byte, right: Byte, operation: (Int, Int) => Int, setCarry: Boolean): Byte = {
      val wideResult = operation(left & 0x0FF,right & 0x0FF)
      val halfResult = operation(left & 0x0F,right & 0x0F)
      val result = (wideResult & 0x0FF).toByte
      Flags.Z.set(result == 0)
      Flags.H.set((halfResult & 0x010) == 0x010)
      if(setCarry) Flags.C.set((wideResult & 0x100) == 0x100)
      result
    }

    def Oper16(left: Short, right: Short, operation: (Int,Int) => Int): Short = {
      val wideResult = operation(left & 0x0FFFF,right & 0x0FFFF)
      val halfResult = operation(left & 0x00FFF,right & 0x00FFF)
      val result = (wideResult & 0x0FFFF).toShort
      Flags.H.set((halfResult & 0x01000) == 0x01000)
      Flags.C.set((wideResult & 0x10000) == 0x10000)
      result
    }
  }

  object Status {
    var stopped: Boolean = false
  }
}
