package com.jhood.sgbc.lr35902.instructions.misc

import com.jhood.sgbc.lr35902.CPU
import com.jhood.sgbc.lr35902.instructions.ImplementedInstruction

object NOP extends ImplementedInstruction {
  override def name: String = "NOP"
  override def cycles: Int = 4
  override def width: Int = 1
  override def execute(cpu: CPU): Unit =
    cpu.incrementPC(this)
}
