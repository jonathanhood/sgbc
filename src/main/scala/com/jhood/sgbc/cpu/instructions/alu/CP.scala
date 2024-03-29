package com.jhood.sgbc.cpu.instructions.alu

import com.jhood.sgbc.cpu.{Operand8, CPU}

case class CP(left: Operand8, right: Operand8) extends ALUInstruction {
  override def name: String = s"CP ${left.name}, ${right.name}"
  override def execute(cpu: CPU): Unit = {
    // Comparison on the lr35902 looks to be a subtraction where flags are
    // set but the result is not retained
    cpu.ALU.Oper8(cpu.read(left),cpu.read(right),_ - _, true)
    cpu.Flags.N.set(true)
  }
}
