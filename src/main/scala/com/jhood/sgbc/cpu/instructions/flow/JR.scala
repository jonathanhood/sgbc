package com.jhood.sgbc.cpu.instructions.flow

import com.jhood.sgbc.cpu.{CPU, Immediate8}
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

case class JR(name: String, condition: (CPU) => Boolean) extends ImplementedInstruction {
  override def cycles: Int = 12
  override def execute(cpu: CPU): Unit = {
    val offset = cpu.read(Immediate8)
    if(condition(cpu)) {
      val addr = cpu.getAndIncrementPC + offset
      cpu.writePC(addr.toShort)
    }
  }
}
