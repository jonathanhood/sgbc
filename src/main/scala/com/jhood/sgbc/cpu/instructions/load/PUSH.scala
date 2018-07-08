package com.jhood.sgbc.cpu.instructions.load

import com.jhood.sgbc.cpu._
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

case class PUSH(register: Register16) extends ImplementedInstruction{
  override def name: String = s"PUSH ${register.name}"
  override def cycles: Int = 16
  override def execute(cpu: CPU): Unit = {
    val value = cpu.read(register)
    val lower = value & 0x0FF
    val upper = (value >> 8) & 0x0FF
    cpu.Registers.decrement(SP)
    cpu.write(Memory8(SP), upper.toByte)
    cpu.Registers.decrement(SP)
    cpu.write(Memory8(SP), lower.toByte)
  }
}
