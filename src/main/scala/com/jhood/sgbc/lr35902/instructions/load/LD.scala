package com.jhood.sgbc.lr35902.instructions.load

import com.jhood.sgbc.lr35902._
import com.jhood.sgbc.lr35902.instructions.ImplementedInstruction

case class LD(target: Operand8, source: Operand8) extends ImplementedInstruction {
  override final val name: String = s"LD ${target.name},${source.name}"
  override final val cycles: Int = source match {
    case _ : Register8 => 4
    case _ : Memory8 => 8
    case Immediate8 => 8
  }
  override final val width: Int = source match {
    case Immediate8 => 2
    case _ => 1
  }
  override def execute(cpu: CPU): Unit = cpu.write(target, cpu.read(source))
}
