package com.jhood.sgbc.lr35902.instructions.load

import com.jhood.sgbc.lr35902.instructions.ImplementedInstruction
import com.jhood.sgbc.lr35902.{Operand16, Registers}
import com.jhood.sgbc.memory.MemoryController

case class LD16(target: Operand16, source: Operand16) extends ImplementedInstruction {
  override def name: String = s"LD ${target.name},${source.name}"
  override def cycles: Int = 8
  override def width: Int = 1
  override def execute(registers: Registers, memory: MemoryController): Unit =
    target.write(source.get)
}
