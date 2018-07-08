package com.jhood.sgbc.cpu.instructions.flow

import com.jhood.sgbc.cpu.CPU
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

object STOP extends ImplementedInstruction {
  override def name: String = "STOP"
  override def cycles: Int = 4
  override def execute(cpu: CPU): Unit = {
    cpu.Status.stopped = true
  }
}
