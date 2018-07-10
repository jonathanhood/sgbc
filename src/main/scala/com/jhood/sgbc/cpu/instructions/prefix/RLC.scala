package com.jhood.sgbc.cpu.instructions.prefix

import com.jhood.sgbc.cpu.{CPU, Operand8}
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

case class RLC(operand: Operand8) extends ImplementedInstruction {
  override def name: String = s"RRC ${operand.name}"
  override def cycles: Int = 4
  override def execute(cpu: CPU): Unit = {
    val original = cpu.read(operand)
    val rotated = ((original & 0x0FF) << 1) | ((original & 0x80) >>> 7)
    cpu.Flags.C.set((original & 0x80) == 0x80)
    cpu.Flags.H.set(false)
    cpu.Flags.N.set(false)
    cpu.Flags.Z.set(false)
    cpu.write(operand, (rotated & 0x0FF).toByte)
  }
}
