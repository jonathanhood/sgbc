package com.jhood.sgbc.lr35902.instructions.alu

import com.jhood.sgbc.lr35902.{Operand8, Registers}
import com.jhood.sgbc.memory.MemoryController

case class ADD(left: Operand8, right: Operand8) extends ALUInstruction {
  // left = left + right
  override def name: String = s"ADD ${left.name},${right.name}"
  override def execute(registers: Registers, memory: MemoryController): Unit = {
    val result = registers.ALU.Oper8(left.get,right.get, _ + _)
    registers.Flags.N.set(false)
    left.write(result)
  }
}

