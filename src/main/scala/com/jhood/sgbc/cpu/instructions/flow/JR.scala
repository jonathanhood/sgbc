package com.jhood.sgbc.cpu.instructions.flow

import com.jhood.sgbc.cpu.{CPU, Immediate8, PC}
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

case class JR(name: String, condition: (CPU) => Boolean) extends ImplementedInstruction {
  override def cycles: Int = 12
  override def width: Int = 2
  override def execute(cpu: CPU): Unit = {
    val addr = cpu.read(PC) + cpu.read(Immediate8)
    if(condition(cpu)) {
      cpu.Registers.write(PC,addr.toShort)
    }
    cpu.incrementPC(this)
  }
}
