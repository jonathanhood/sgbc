package com.jhood.sgbc.cpu.instructions.misc

import com.jhood.sgbc.cpu.CPU
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

object SCF extends ImplementedInstruction {
  override def name: String = "SCF"
  override def cycles: Int = 4
  override def execute(cpu: CPU): Unit = cpu.Flags.C.set(true)
}
