package com.jhood.sgbc.lr35902

import com.jhood.sgbc.memory.MemoryController

trait Operand8 {
  def name: String
  def get: Byte
  def write(value: Byte): Unit
}

trait Register8 extends Operand8 {
  def increment(value: Int): Unit = {
    write((get + value).toByte)
  }
}

case class Memory8(addrSource: Register16, memory: MemoryController, offset: Int = 0) extends Operand8 {
  private def addr = (addrSource.get + offset).toShort
  override def name: String = s"(${addrSource.name})"
  override def get: Byte = memory.fetch(addr)
  override def write(value: Byte): Unit = memory.write(addr,value)
}

trait Operand16 {
  def name: String
  def get: Short
  def write(value: Short): Unit
}

trait Register16 extends Operand16 {
  def increment(value: Int): Unit = {
    write((get + value).toShort)
  }
}

case class Memory16(addrSource: Register16, memory: MemoryController, offset: Int = 0) extends Operand16 {
  private def addr = (addrSource.get + offset).toShort
  override def name: String = s"(${addrSource.name})"
  override def get: Short = memory.fetchShort(addr)
  override def write(value: Short): Unit = memory.writeShort(addr,value)
}

trait Flag {
  def name: String
  def set(value: Boolean): Unit
  def isSet: Boolean
  def get: Byte = if(isSet) 1 else 0
}

class Registers {
  // AF, BC, DE, HL all fit into a single 64-bit

  // Setting to 0x0100 initially identifies this as an OG Gameboy
  //    0x1100 would be GBC (which we aren't implementing)
  //    0xFF00 is SGB (which we also aren't implementing)
  private var registers : Long = 0x0100

  case class Register8Impl(name: String, offset: Byte) extends Register8 {
    def get: Byte = (registers >>> offset).toByte
    def write(value: Byte): Unit = {
      val mask: Long = ~(0xFFl << offset)
      registers = (registers & mask) | (value.toLong << offset)
    }
  }

  case class Register16Impl(name: String, offset: Byte) extends Register16 {
    def get: Short = (registers >>> offset).toShort
    def write(value: Short): Unit = {
      val mask: Long = ~(0xFFFFl << offset)
      registers = (registers & mask) | (value.toLong << offset)
    }
  }

  case class FlagImpl(name: String, offset: Byte) extends Flag {
    override def set(value: Boolean): Unit =
      if(value) {
        val mask: Long = 1l << offset
        registers = registers | mask
      } else {
        val mask: Long = ~(1l << offset)
        registers = registers & mask
      }

    override def isSet: Boolean =
      ((registers >>> offset) & 0x01) == 0x01
  }

  object PC extends Register16 {
    private var pc: Short = 0
    override def get: Short = pc
    override def write(value: Short): Unit = pc = value
    override val name: String = "PC"
  }

  object SP extends Register16 {
    private var sp: Short = 0
    override def get: Short = sp
    override def write(value: Short): Unit = sp = value
    override val name: String = "SP"
  }

  object Flags {
    object Z extends FlagImpl("Z", 7)
    object N extends FlagImpl("N", 6)
    object H extends FlagImpl("H", 5)
    object C extends FlagImpl("C", 4)
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

  object F extends Register8Impl("F",0)
  object A extends Register8Impl("A",8)
  object C extends Register8Impl("C",16)
  object B extends Register8Impl("B",24)
  object E extends Register8Impl("E",32)
  object D extends Register8Impl("D",40)
  object L extends Register8Impl("L",48)
  object H extends Register8Impl("H",56)

  object AF extends Register16Impl("AF",0)
  object BC extends Register16Impl("BC",16)
  object DE extends Register16Impl("DE",32)
  object HL extends Register16Impl("HL",48)
}
