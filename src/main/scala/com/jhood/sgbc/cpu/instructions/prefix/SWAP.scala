package com.jhood.sgbc.cpu.instructions.prefix

import com.jhood.sgbc.cpu.{CPU, Operand8}
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

case class SWAP(operand: Operand8) extends ImplementedInstruction {
  override def name: String = s"SWAP ${operand}"
  override def cycles: Int = 4
  override def execute(cpu: CPU): Unit = {
    val original = cpu.read(operand)
    val lower = original & 0x0F
    val upper = (original >>> 4) & 0x0F
    val swapped = ((lower << 8) | upper).toByte

    cpu.Flags.H.set(false)
    cpu.Flags.C.set(false)
    cpu.Flags.N.set(false)
    cpu.Flags.Z.set(swapped == 0)

    cpu.write(operand, swapped)
  }
}
