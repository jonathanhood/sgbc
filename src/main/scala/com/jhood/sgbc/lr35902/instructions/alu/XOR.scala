package com.jhood.sgbc.lr35902.instructions.alu

import com.jhood.sgbc.lr35902.{Operand8, Registers}
import com.jhood.sgbc.memory.MemoryController

case class XOR(left: Operand8, right: Operand8) extends ALUInstruction {
  override def name: String = s"XOR ${left.name}, ${right.name}"
  override def execute(registers: Registers, memory: MemoryController): Unit = {
    val result = (left.get ^ right.get).toByte
    registers.Flags.Z.set(result == 0)
    registers.Flags.N.set(false)
    registers.Flags.H.set(false)
    registers.Flags.C.set(false)
    left.write(result)
  }
}
