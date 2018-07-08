package com.jhood.sgbc.cpu.instructions.prefix

import com.jhood.sgbc.cpu.{CPU, Operand8}
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

case class RR(operand: Operand8) extends ImplementedInstruction {
  override def name: String = s"RR ${operand.name}"
  override def cycles: Int = 4
  override def execute(cpu: CPU): Unit = {
    val original = cpu.read(operand)
    val rotated = ((original & 0x0FF) >>> 1) | (cpu.Flags.C.get << 7)
    cpu.Flags.C.set((original & 0x01) == 0x01)
    cpu.Flags.H.set(false)
    cpu.Flags.N.set(false)
    cpu.Flags.Z.set(rotated == 0)
    cpu.write(operand, (rotated & 0x0FF).toByte)
  }
}
