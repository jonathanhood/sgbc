package com.jhood.sgbc.cpu.instructions.alu

import com.jhood.sgbc.cpu.{Operand8, CPU}

case class XOR(left: Operand8, right: Operand8) extends ALUInstruction {
  override def name: String = s"XOR ${left.name}, ${right.name}"
  override def execute(cpu: CPU): Unit = {
    val result = (cpu.read(left) ^ cpu.read(right)).toByte
    cpu.Flags.Z.set(result == 0)
    cpu.Flags.N.set(false)
    cpu.Flags.H.set(false)
    cpu.Flags.C.set(false)
    cpu.write(left,result)
    cpu.incrementPC(this)
  }
}
