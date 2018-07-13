package com.jhood.sgbc.cpu.instructions.alu

import com.jhood.sgbc.cpu.instructions.ImplementedInstruction
import com.jhood.sgbc.cpu.{CPU, Operand16}

case class ADD16(left: Operand16, right: Operand16, clearZero: Boolean) extends ImplementedInstruction {
  // left = left + right
  override def name: String = s"ADD ${left.name},${right.name}"
  override def execute(cpu: CPU): Unit = {
    val result = cpu.ALU.Oper16(cpu.read(left),cpu.read(right), _ + _)
    cpu.Flags.N.set(false)
    if(clearZero) cpu.Flags.Z.set(false)
    cpu.write(left, result)
  }

  override def cycles: Int = 12
}