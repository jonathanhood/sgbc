package com.jhood.sgbc.cpu.instructions.alu

import com.jhood.sgbc.cpu.{Operand8, CPU}

case class SUB(left: Operand8, right: Operand8) extends ALUInstruction {
  // left = left - right
  override def name: String = s"SUB ${left.name}, ${right.name}"
  override def execute(cpu: CPU): Unit = {
    val result = cpu.ALU.Oper8(cpu.read(left),cpu.read(right), _ - _, true)
    cpu.Flags.N.set(true)
    cpu.write(left,result)
  }
}
