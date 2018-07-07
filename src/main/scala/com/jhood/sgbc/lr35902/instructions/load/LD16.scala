package com.jhood.sgbc.lr35902.instructions.load

import com.jhood.sgbc.lr35902.instructions.ImplementedInstruction
import com.jhood.sgbc.lr35902.{CPU, Immediate16, Operand16, Register16}

case class LD16(target: Register16, source: Operand16) extends ImplementedInstruction {
  override def name: String = s"LD ${target.name},${source.name}"
  override def cycles: Int = 12
  override def width: Int = source match {
    case Immediate16 => 3
    case _ => 1
  }
  override def execute(cpu: CPU): Unit =
    cpu.Registers.write(target, cpu.read(source))
}
