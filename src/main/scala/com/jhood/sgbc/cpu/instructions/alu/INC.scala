package com.jhood.sgbc.cpu.instructions.alu

import com.jhood.sgbc.cpu.{CPU, Operand8}

case class INC(operand: Operand8) extends ALUInstruction {
  override def left: Operand8 = operand
  override def right: Operand8 = operand
  override def name: String = s"INC ${operand.name}"
  override def execute(cpu: CPU): Unit = {
    val result = cpu.ALU.Oper8(cpu.read(operand), 1.toByte, _ + _)
    cpu.Flags.N.set(false)
    cpu.write(operand, result)
  }
}
