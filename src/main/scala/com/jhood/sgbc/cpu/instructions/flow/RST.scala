package com.jhood.sgbc.cpu.instructions.flow

import com.jhood.sgbc.cpu.{CPU, Memory8, SP}
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

case class RST(addr: Byte) extends ImplementedInstruction {
  override def name: String = s"RST 0x${(addr & 0x0FF).toHexString}"
  override def cycles: Int = 16
  override def execute(cpu: CPU): Unit = {
    push(cpu)
    cpu.writePC((addr & 0x0FF).toShort)
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
