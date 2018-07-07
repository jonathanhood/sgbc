package com.jhood.sgbc.lr35902.instructions.alu

import com.jhood.sgbc.lr35902.{Operand8, CPU}

case class SBC(left: Operand8, right: Operand8) extends ALUInstruction {
  // left = left - right
  override def name: String = s"SBC ${left.name}, ${right.name}"
  override def execute(cpu: CPU): Unit = {
    val withCarry = (cpu.read(right) + cpu.Flags.C.get).toByte
    val result = cpu.ALU.Oper8(cpu.read(left),withCarry, _ - _)
    cpu.Flags.N.set(true)
    cpu.write(left,result)
    cpu.incrementPC(this)
  }
}
