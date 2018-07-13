package com.jhood.sgbc.cpu.instructions.load

import com.jhood.sgbc.cpu.instructions.ImplementedInstruction
import com.jhood.sgbc.cpu.{CPU, Immediate16, Operand16, Register16}

case class LD16(target: Operand16, source: Operand16) extends ImplementedInstruction {
  override def name: String = s"LD ${target.name},${source.name}"
  override def cycles: Int = 12

  override def execute(cpu: CPU): Unit = {
    cpu.write(target, cpu.read(source))
  }
}
