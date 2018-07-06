package com.jhood.sgbc.lr35902.instructions.alu

import com.jhood.sgbc.lr35902.{Flag, Operand8, Registers}
import com.jhood.sgbc.memory.MemoryController

case class ADC(left: Operand8, right: Operand8) extends ALUInstruction {
  // left = left + right + C
  override def name: String = s"ADC ${left.name},${right.name}"
  override def execute(registers: Registers, memory: MemoryController): Unit = {
    val result = registers.ALU.Oper8(left.get, right.get, _ + _)
    registers.Flags.N.set(false)
    left.write(result)
  }

  private def halfCarry(carry: Flag): Boolean =
    (((left.get & 0x0F) + ((right.get + carry.get) & 0x0F)) & 0x10) == 0x10

  private def carry(result: Int): Boolean =
    (result & 0x100) == 0x100
}
