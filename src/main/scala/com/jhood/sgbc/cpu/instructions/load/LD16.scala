package com.jhood.sgbc.cpu.instructions.load

import com.jhood.sgbc.cpu.instructions.ImplementedInstruction
import com.jhood.sgbc.cpu.{CPU, Immediate16, Operand16, Register16}

case class LD16(target: Register16, source: Operand16) extends ImplementedInstruction {
  override def name: String = s"LD ${target.name},${source.name}"
  override def cycles: Int = 12
  override def width: Int = source match {
    case Immediate16 => 3
    case _ => 1
  }
  override def execute(cpu: CPU): Unit = {
    cpu.Registers.write(target, cpu.read(source))
    cpu.incrementPC(this)
  }
}
