package com.jhood.sgbc.cpu.instructions.flow

import com.jhood.sgbc.cpu.{CPU, Operand16}
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

case class JP(operand: Operand16, name: String, condition: (CPU) => Boolean) extends ImplementedInstruction {
  override def cycles: Int = 16
  override def execute(cpu: CPU): Unit = {
    val addr = cpu.read(operand)
    if(condition(cpu)) {
      cpu.writePC(addr)
    }
  }
}
