package com.jhood.sgbc.cpu.instructions.flow

import com.jhood.sgbc.cpu.{CPU, Memory8, PC, SP}
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

case class RET(name: String, condition: (CPU) => Boolean) extends ImplementedInstruction {
  override def cycles: Int = 20
  override def width: Int = 1
  override def execute(cpu: CPU): Unit = {
    if(condition(cpu)) {
      val lower = cpu.read(Memory8(SP)) & 0x0FF
      cpu.Registers.increment(SP)
      val upper = cpu.read(Memory8(SP)) & 0x0FF
      cpu.Registers.increment(SP)

      val combined = (upper << 8) + lower
      cpu.Registers.write(PC,combined.toShort)
    } else {
      cpu.incrementPC(this)
    }
  }
}
