package com.jhood.sgbc.cpu.instructions.prefix

import com.jhood.sgbc.cpu.{CPU, Operand8}
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

case class SRL(operand: Operand8) extends ImplementedInstruction {
  override def name: String = s"SRL ${operand.name}"
  override def cycles: Int = 4

  override def execute(cpu: CPU): Unit = {
    val value = cpu.read(operand)
    val shifted = (value & 0x0FF) >> 1
    cpu.Flags.Z.set(shifted == 0)
    cpu.Flags.N.set(false)
    cpu.Flags.H.set(false)
    cpu.Flags.C.set((value & 0x01) == 0x01)
    cpu.write(operand, shifted.toByte)
  }
}
