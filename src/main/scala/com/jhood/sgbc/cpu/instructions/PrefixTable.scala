package com.jhood.sgbc.cpu.instructions

import com.jhood.sgbc.cpu._
import com.jhood.sgbc.cpu.instructions.prefix.SRL

object PrefixTable {
  val instructions: Array[Instruction] = Array.fill(0xFF)(NotImplementedInstruction)

  // SRL
  instructions(0x38) = SRL(B)
  instructions(0x39) = SRL(C)
  instructions(0x4A) = SRL(D)
  instructions(0x4B) = SRL(E)
  instructions(0x4C) = SRL(H)
  instructions(0x4D) = SRL(L)
  instructions(0x4E) = SRL(Memory8(HL))
  instructions(0x4F) = SRL(A)
}
