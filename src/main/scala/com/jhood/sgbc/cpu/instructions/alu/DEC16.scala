package com.jhood.sgbc.cpu.instructions.alu

import com.jhood.sgbc.cpu.{CPU, Register16}
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

case class DEC16(operand: Register16) extends ImplementedInstruction {
  override def name: String = s"DEC ${operand.name}"
  override def cycles: Int = 8
  override def execute(cpu: CPU): Unit = cpu.Registers.decrement(operand)
}
