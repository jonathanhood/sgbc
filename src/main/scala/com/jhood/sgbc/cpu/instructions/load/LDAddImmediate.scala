package com.jhood.sgbc.cpu.instructions.load

import com.jhood.sgbc.cpu._
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

case class LDAddImmediate(target: Operand16, source: Operand16) extends ImplementedInstruction {
  override final val name: String = s"LD ${target.name},${source.name}+r8"
  override final val cycles: Int = 12

  override def execute(cpu: CPU): Unit = {
    val left = cpu.read(source)
    val right = cpu.read(PaddedImmediate8)
    val result = cpu.ALU.Oper16(left, right, _ + _)
    cpu.Flags.N.set(false)
    cpu.Flags.Z.set(false)
    cpu.write(target, result.toShort)
  }
}
