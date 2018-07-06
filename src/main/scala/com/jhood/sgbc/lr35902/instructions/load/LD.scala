package com.jhood.sgbc.lr35902.instructions.load

import com.jhood.sgbc.lr35902._
import com.jhood.sgbc.lr35902.instructions.ImplementedInstruction
import com.jhood.sgbc.memory.MemoryController

case class LD(target: Operand8, source: Operand8) extends ImplementedInstruction {
  override final val name: String = s"LD ${target.name},${source.name}"
  override final val cycles: Int = source match {
    case _ : Register8 => 4
    case _ : Memory8 => 8
  }
  override final val width: Int = 1
  override def execute(registers: Registers, memory: MemoryController): Unit =  {
    target.write(source.get)
  }
}
