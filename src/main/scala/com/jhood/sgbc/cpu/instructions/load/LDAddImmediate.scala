package com.jhood.sgbc.cpu.instructions.load

import com.jhood.sgbc.cpu._
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

case class LDAddImmediate(target: Operand16, source: Operand16) extends ImplementedInstruction {
  override final val name: String = s"LD ${target.name},${source.name}+r8"
  override final val cycles: Int = 12

  override def execute(cpu: CPU): Unit = {
    val imm = cpu.read(Immediate8)
    val result = (cpu.read(source) + imm) & 0x0FFFF
    cpu.write(target, result.toShort)
  }
}
