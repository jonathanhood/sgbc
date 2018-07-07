package com.jhood.sgbc.lr35902.instructions.alu

import com.jhood.sgbc.lr35902.{Operand8, CPU}

case class ADC(left: Operand8, right: Operand8) extends ALUInstruction {
  // left = left + right + C
  override def name: String = s"ADC ${left.name},${right.name}"
  override def execute(cpu: CPU): Unit = {
    val result = cpu.ALU.Oper8(cpu.read(left), cpu.read(right), _ + _)
    cpu.Flags.N.set(false)
    cpu.write(left, result)
  }
}
