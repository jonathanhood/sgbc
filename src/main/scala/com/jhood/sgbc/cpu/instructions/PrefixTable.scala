package com.jhood.sgbc.cpu.instructions

import com.jhood.sgbc.cpu._
import com.jhood.sgbc.cpu.instructions.prefix._

object PrefixTable {
  val instructions: Array[Instruction] = Array.fill(0xFF)(NotImplementedInstruction)

  //RLC
  instructions(0x00) = RLC(B)
  instructions(0x01) = RLC(C)
  instructions(0x02) = RLC(D)
  instructions(0x03) = RLC(E)
  instructions(0x04) = RLC(H)
  instructions(0x05) = RLC(L)
  instructions(0x06) = RLC(Memory8(HL))
  instructions(0x07) = RLC(A)

  // RRC
  instructions(0x08) = RRC(B)
  instructions(0x09) = RRC(C)
  instructions(0x0A) = RRC(D)
  instructions(0x0B) = RRC(E)
  instructions(0x0C) = RRC(H)
  instructions(0x0D) = RRC(L)
  instructions(0x0E) = RRC(Memory8(HL))
  instructions(0x0F) = RRC(A)

  // RR
  instructions(0x18) = RR(B)
  instructions(0x19) = RR(C)
  instructions(0x1A) = RR(D)
  instructions(0x1B) = RR(E)
  instructions(0x1C) = RR(H)
  instructions(0x1D) = RR(L)
  instructions(0x1E) = RR(Memory8(HL))
  instructions(0x1F) = RR(A)

  // SWAP
  instructions(0x30) = SWAP(B)
  instructions(0x31) = SWAP(C)
  instructions(0x32) = SWAP(D)
  instructions(0x33) = SWAP(E)
  instructions(0x34) = SWAP(H)
  instructions(0x35) = SWAP(L)
  instructions(0x36) = SWAP(Memory8(HL))
  instructions(0x37) = SWAP(A)

  // SRA
  instructions(0x28) = SRA(B)
  instructions(0x29) = SRA(C)
  instructions(0x2A) = SRA(D)
  instructions(0x2B) = SRA(E)
  instructions(0x2C) = SRA(H)
  instructions(0x2D) = SRA(L)
  instructions(0x2E) = SRA(Memory8(HL))
  instructions(0x2F) = SRA(A)

  // SRL
  instructions(0x38) = SRL(B)
  instructions(0x39) = SRL(C)
  instructions(0x3A) = SRL(D)
  instructions(0x3B) = SRL(E)
  instructions(0x3C) = SRL(H)
  instructions(0x3D) = SRL(L)
  instructions(0x3E) = SRL(Memory8(HL))
  instructions(0x3F) = SRL(A)
}
