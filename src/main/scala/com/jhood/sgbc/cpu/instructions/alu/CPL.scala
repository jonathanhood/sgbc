package com.jhood.sgbc.cpu.instructions.alu

import com.jhood.sgbc.cpu.{A, CPU}
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

object CPL extends ImplementedInstruction {
  override def name: String = "CPL"
  override def cycles: Int = 4
  override def execute(cpu: CPU): Unit = {
    cpu.write(A, (~cpu.read(A)).toByte)
    cpu.Flags.N.set(true)
    cpu.Flags.H.set(true)
  }
}
