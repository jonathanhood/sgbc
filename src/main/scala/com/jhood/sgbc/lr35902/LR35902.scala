package com.jhood.sgbc.lr35902

import com.jhood.sgbc.memory.MemoryController

trait Instruction {
  def name: String
  def cycles: Int
  def width: Int
  def execute(registers: Registers, memory: MemoryController)
}

case class LoadRegisterWithImmediate16(target: Register16) extends Instruction {
  override def name: String = s"LD ${target.name},d16"
  override def cycles: Int = 12
  override def width: Int = 3
  override def execute(registers: Registers, memory: MemoryController): Unit = {
    val data = memory.fetchShort((registers.PC.get + 1).toShort)
    target.write(data)
  }
}

case class LoadRegisterWithImmediate8(target: Register8) extends Instruction {
  override def name: String = s"LD ${target.name},d8"
  override def cycles: Int = 8
  override def width: Int = 2
  override def execute(registers: Registers, memory: MemoryController): Unit = {
    val data = memory.fetch((registers.PC.get + 1).toShort)
    target.write(data)
  }
}

case class LoadRegisterFromMemory8(source: Register16, target: Register8) extends Instruction {
  override def name: String = s"LD (${source.name}),${target.name}"
  override def cycles: Int = 8
  override def width: Int = 1
  override def execute(registers: Registers, memory: MemoryController): Unit =  {
    val data = memory.fetch(source.get)
    target.write(data)
  }
}

case class LoadRegisterFromMemory8AndUpdate(source: Register16, target: Register8, modifier: Int) extends Instruction {
  override def name: String = if(modifier > 0) s"LD (${source.name}+),${target.name}" else s"LD (${source.name}-),${target.name}"
  override def cycles: Int = 8
  override def width: Int = 1
  override def execute(registers: Registers, memory: MemoryController): Unit =  {
    val data = memory.fetch(source.get)
    target.write(data)
    source.increment(modifier)
  }
}

case class LoadMemoryFromRegister8(source: Register8, target: Register16) extends Instruction {
  override def name: String = s"LD ${source.name},(${target.name})"
  override def cycles: Int = 8
  override def width: Int = 1
  override def execute(registers: Registers, memory: MemoryController): Unit =
    memory.write(target.get, source.get)
}

case class CopyRegister(source: Register8, target: Register8) extends Instruction {
  override def name: String = s"LD ${source.name},${target.name}"
  override def cycles: Int = 4
  override def width: Int = 1
  override def execute(registers: Registers, memory: MemoryController): Unit =
    target.write(source.get)
}

case class AddRegisters8(left: Register8, right: Register8) extends Instruction {
  // left = left + right

  override def name: String = s"ADD ${left.name},${right.name}"
  override def cycles: Int = 4
  override def width: Int = 1
  override def execute(registers: Registers, memory: MemoryController): Unit = {
    val result = left.get + right.get
    registers.Flags.set(result == 0, false, halfCarry, carry(result))
    left.write(result.toByte)
  }

  private def halfCarry: Boolean =
    (((left.get & 0x0F) + (right.get & 0x0F)) & 0x10) == 0x10

  private def carry(result: Int): Boolean =
    (result & 0x100) == 0x100
}

case class AddRegisters8WithCarry(left: Register8, right: Register8) extends Instruction {
  // left = left + right + C

  override def name: String = s"ADC ${left.name},${right.name}"
  override def cycles: Int = 4
  override def width: Int = 1
  override def execute(registers: Registers, memory: MemoryController): Unit = {
    val result = left.get + right.get + registers.Flags.C.get
    registers.Flags.set(result == 0, false, halfCarry(registers.Flags.C), carry(result))
    left.write(result.toByte)
  }

  private def halfCarry(carry: Flag): Boolean =
    (((left.get & 0x0F) + ((right.get + carry.get) & 0x0F)) & 0x10) == 0x10

  private def carry(result: Int): Boolean =
    (result & 0x100) == 0x100
}

object NOP extends Instruction {
  override def name: String = "NOP"
  override def cycles: Int = 4
  override def width: Int = 1
  override def execute(registers: Registers, memory: MemoryController): Unit = ()
}

class LR35902(registers: Registers, memory: MemoryController) {
  private val instructions: Array[Instruction] = Array.fill(0xFF)(NOP)

  // LD XX,d16
  instructions(0x01) = LoadRegisterWithImmediate16(registers.BC)
  instructions(0x11) = LoadRegisterWithImmediate16(registers.DE)
  instructions(0x21) = LoadRegisterWithImmediate16(registers.HL)
  instructions(0x31) = LoadRegisterWithImmediate16(registers.SP)

  // LD X,d8
  instructions(0x06) = LoadRegisterWithImmediate8(registers.B)
  instructions(0x16) = LoadRegisterWithImmediate8(registers.D)
  instructions(0x26) = LoadRegisterWithImmediate8(registers.H)
  instructions(0x0E) = LoadRegisterWithImmediate8(registers.C)
  instructions(0x1E) = LoadRegisterWithImmediate8(registers.E)
  instructions(0x2E) = LoadRegisterWithImmediate8(registers.L)
  instructions(0x3E) = LoadRegisterWithImmediate8(registers.A)

  // LD B,X
  instructions(0x40) = CopyRegister(registers.B,registers.B)
  instructions(0x41) = CopyRegister(registers.B,registers.C)
  instructions(0x42) = CopyRegister(registers.B,registers.D)
  instructions(0x43) = CopyRegister(registers.B,registers.E)
  instructions(0x44) = CopyRegister(registers.B,registers.H)
  instructions(0x45) = CopyRegister(registers.B,registers.L)
  instructions(0x47) = CopyRegister(registers.B,registers.A)

  // LD C,X
  instructions(0x48) = CopyRegister(registers.C,registers.B)
  instructions(0x49) = CopyRegister(registers.C,registers.C)
  instructions(0x4A) = CopyRegister(registers.C,registers.D)
  instructions(0x4B) = CopyRegister(registers.C,registers.E)
  instructions(0x4C) = CopyRegister(registers.C,registers.H)
  instructions(0x4D) = CopyRegister(registers.C,registers.L)
  instructions(0x4F) = CopyRegister(registers.C,registers.A)

  // LD D,X
  instructions(0x50) = CopyRegister(registers.D,registers.B)
  instructions(0x51) = CopyRegister(registers.D,registers.C)
  instructions(0x52) = CopyRegister(registers.D,registers.D)
  instructions(0x53) = CopyRegister(registers.D,registers.E)
  instructions(0x54) = CopyRegister(registers.D,registers.H)
  instructions(0x55) = CopyRegister(registers.D,registers.L)
  instructions(0x57) = CopyRegister(registers.D,registers.A)

  // LD E,X
  instructions(0x58) = CopyRegister(registers.E,registers.B)
  instructions(0x59) = CopyRegister(registers.E,registers.C)
  instructions(0x5A) = CopyRegister(registers.E,registers.D)
  instructions(0x5B) = CopyRegister(registers.E,registers.E)
  instructions(0x5C) = CopyRegister(registers.E,registers.H)
  instructions(0x5D) = CopyRegister(registers.E,registers.L)
  instructions(0x5F) = CopyRegister(registers.E,registers.A)

  // LD H,X
  instructions(0x60) = CopyRegister(registers.H,registers.B)
  instructions(0x61) = CopyRegister(registers.H,registers.C)
  instructions(0x62) = CopyRegister(registers.H,registers.D)
  instructions(0x63) = CopyRegister(registers.H,registers.E)
  instructions(0x64) = CopyRegister(registers.H,registers.H)
  instructions(0x65) = CopyRegister(registers.H,registers.L)
  instructions(0x67) = CopyRegister(registers.H,registers.A)

  // LD L,X
  instructions(0x68) = CopyRegister(registers.L,registers.B)
  instructions(0x69) = CopyRegister(registers.L,registers.C)
  instructions(0x6A) = CopyRegister(registers.L,registers.D)
  instructions(0x6B) = CopyRegister(registers.L,registers.E)
  instructions(0x6C) = CopyRegister(registers.L,registers.H)
  instructions(0x6D) = CopyRegister(registers.L,registers.L)
  instructions(0x6F) = CopyRegister(registers.L,registers.A)

  // LD A,X
  instructions(0x78) = CopyRegister(registers.A,registers.B)
  instructions(0x79) = CopyRegister(registers.A,registers.C)
  instructions(0x7A) = CopyRegister(registers.A,registers.D)
  instructions(0x7B) = CopyRegister(registers.A,registers.E)
  instructions(0x7C) = CopyRegister(registers.A,registers.H)
  instructions(0x7D) = CopyRegister(registers.A,registers.L)
  instructions(0x7F) = CopyRegister(registers.A,registers.A)

  // LD (XX),Y
  instructions(0x02) = LoadRegisterFromMemory8(registers.BC, registers.A)
  instructions(0x12) = LoadRegisterFromMemory8(registers.DE, registers.A)
  instructions(0x22) = LoadRegisterFromMemory8AndUpdate(registers.HL, registers.A, 1)
  instructions(0x32) = LoadRegisterFromMemory8AndUpdate(registers.HL, registers.A, -1)
  instructions(0x70) = LoadRegisterFromMemory8(registers.HL,registers.B)
  instructions(0x71) = LoadRegisterFromMemory8(registers.HL,registers.C)
  instructions(0x72) = LoadRegisterFromMemory8(registers.HL,registers.D)
  instructions(0x73) = LoadRegisterFromMemory8(registers.HL,registers.E)
  instructions(0x74) = LoadRegisterFromMemory8(registers.HL,registers.H)
  instructions(0x75) = LoadRegisterFromMemory8(registers.HL,registers.L)
  instructions(0x77) = LoadRegisterFromMemory8(registers.HL,registers.A)

  // LD X,(YY)
  instructions(0x46) = LoadMemoryFromRegister8(registers.B, registers.HL)
  instructions(0x56) = LoadMemoryFromRegister8(registers.D, registers.HL)
  instructions(0x66) = LoadMemoryFromRegister8(registers.H, registers.HL)
  instructions(0x4E) = LoadMemoryFromRegister8(registers.C, registers.HL)
  instructions(0x5E) = LoadMemoryFromRegister8(registers.E, registers.HL)
  instructions(0x6E) = LoadMemoryFromRegister8(registers.L, registers.HL)
  instructions(0x7E) = LoadMemoryFromRegister8(registers.A, registers.HL)

  // ADD A,X
  instructions(0x80) = AddRegisters8(registers.A,registers.B)
  instructions(0x81) = AddRegisters8(registers.A,registers.C)
  instructions(0x82) = AddRegisters8(registers.A,registers.D)
  instructions(0x83) = AddRegisters8(registers.A,registers.E)
  instructions(0x84) = AddRegisters8(registers.A,registers.H)
  instructions(0x85) = AddRegisters8(registers.A,registers.L)
  instructions(0x87) = AddRegisters8(registers.A,registers.A)

  // ADC A,X
  instructions(0x88) = AddRegisters8WithCarry(registers.A,registers.B)
  instructions(0x89) = AddRegisters8WithCarry(registers.A,registers.C)
  instructions(0x8A) = AddRegisters8WithCarry(registers.A,registers.D)
  instructions(0x8B) = AddRegisters8WithCarry(registers.A,registers.E)
  instructions(0x8C) = AddRegisters8WithCarry(registers.A,registers.H)
  instructions(0x8D) = AddRegisters8WithCarry(registers.A,registers.L)
  instructions(0x8F) = AddRegisters8WithCarry(registers.A,registers.A)


  def tick: Unit = {
    val inst = instructions(memory.fetch(registers.PC.get))
    inst.execute(registers, memory)
    registers.PC.increment(inst.width)
  }
}
