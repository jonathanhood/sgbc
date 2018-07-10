package com.jhood.sgbc.cpu.instructions.flow

import com.jhood.sgbc.cpu.CPU
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

object DI extends ImplementedInstruction  {
  override def name: String = "DI"
  override def cycles: Int = 4
  override def execute(cpu: CPU): Unit =
    cpu.interruptController.setMasterEnable(false)
}
