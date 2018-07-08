package com.jhood.sgbc.cpu.instructions.flow

import com.jhood.sgbc.cpu._
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

case class CALL(name: String, condition: (CPU) => Boolean) extends ImplementedInstruction {
  override def cycles: Int = 20
  override def execute(cpu: CPU): Unit = {
    val jumpAddr = cpu.read(Immediate16)
    if(condition(cpu)) {
      push(cpu)
      cpu.writePC(jumpAddr)
    }
  }

  private def push(cpu: CPU): Unit = {
    val pc = cpu.getAndIncrementPC
    val lower = pc & 0x0FF
    val upper = (pc >> 8) & 0x0FF
    cpu.Registers.decrement(SP)
    cpu.write(Memory8(SP), upper.toByte)
    cpu.Registers.decrement(SP)
    cpu.write(Memory8(SP), lower.toByte)
  }
}
