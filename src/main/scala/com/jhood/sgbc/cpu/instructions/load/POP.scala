package com.jhood.sgbc.cpu.instructions.load

import com.jhood.sgbc.cpu._
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

case class POP(register: Register16) extends ImplementedInstruction {
  override def name: String = s"POP ${register.name}"
  override def cycles: Int = 12
  override def execute(cpu: CPU): Unit = {
    val lower = cpu.read(Memory8(SP)) & 0x0FF
    cpu.Registers.increment(SP)
    val upper = cpu.read(Memory8(SP)) & 0x0FF
    cpu.Registers.increment(SP)
    val combined = (upper << 8) + lower

    cpu.Registers.write(register,combined.toShort)
  }
}
