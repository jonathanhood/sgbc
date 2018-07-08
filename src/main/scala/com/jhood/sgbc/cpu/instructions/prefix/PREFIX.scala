package com.jhood.sgbc.cpu.instructions.prefix

import com.jhood.sgbc.cpu.{CPU, Memory8, PC}
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

object PREFIX extends ImplementedInstruction {
  override def name: String = "PREFIX"
  override def cycles: Int = 4
  override def width: Int = 1
  override def execute(cpu: CPU): Unit = {
    cpu.Registers.increment(PC)
    val prefixCode = cpu.read(Memory8(PC)) & 0x0FF
    throw new Exception(s"Prefix instruction not implemented 0x${prefixCode.toHexString}")
  }
}
