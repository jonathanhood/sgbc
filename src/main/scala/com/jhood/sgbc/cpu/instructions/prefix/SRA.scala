package com.jhood.sgbc.cpu.instructions.prefix

import com.jhood.sgbc.cpu.instructions.ImplementedInstruction
import com.jhood.sgbc.cpu.{CPU, Operand8}

case class SRA(operand: Operand8) extends ImplementedInstruction {
  override def name: String = s"SRA ${operand.name}"
  override def cycles: Int = 4

  override def execute(cpu: CPU): Unit = {
    val value = cpu.read(operand)
    val shifted = (value & 0x0FF) >>> 1 | (value & 0x80)
    cpu.Flags.Z.set(value == 0)
    cpu.Flags.N.set(false)
    cpu.Flags.H.set(false)
    cpu.Flags.C.set((value & 0x01) == 0x01)
    cpu.write(operand, shifted.toByte)
  }
}
