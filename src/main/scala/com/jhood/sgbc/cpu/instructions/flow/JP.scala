package com.jhood.sgbc.cpu.instructions.flow

import com.jhood.sgbc.cpu.{CPU, Immediate16, PC}
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

case class JP(name: String, condition: (CPU) => Boolean) extends ImplementedInstruction {
  override def cycles: Int = 16
  override def width: Int = 3
  override def execute(cpu: CPU): Unit = {
    val addr = cpu.read(Immediate16)
    if(condition(cpu)) {
      cpu.Registers.write(PC,addr)
    }
    cpu.incrementPC(this)
  }
}
