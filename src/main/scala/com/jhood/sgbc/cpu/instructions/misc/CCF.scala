package com.jhood.sgbc.cpu.instructions.misc

import com.jhood.sgbc.cpu.CPU
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

object CCF extends ImplementedInstruction {
  override def name: String = "CCF"
  override def cycles: Int = 4
  override def execute(cpu: CPU): Unit = cpu.Flags.C.set(!cpu.Flags.C.isSet)
}
