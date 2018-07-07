package com.jhood.sgbc.lr35902.instructions

import com.jhood.sgbc.lr35902._
import com.jhood.sgbc.lr35902.instructions.alu._
import com.jhood.sgbc.lr35902.instructions.flow.JP
import com.jhood.sgbc.lr35902.instructions.load.{LD, LD16}
import com.jhood.sgbc.lr35902.instructions.misc.NOP

object InstructionTable {
  val instructions: Array[Instruction] = Array.fill(0xFF)(NotImplementedInstruction)

  // Misc
  instructions(0x00) = NOP

  // LD XX,d16
  instructions(0x01) = LD16(BC,Immediate16)
  instructions(0x11) = LD16(DE,Immediate16)
  instructions(0x21) = LD16(HL,Immediate16)
  instructions(0x31) = LD16(SP,Immediate16)

  // LD X,d8
  instructions(0x06) = LD(B,Immediate8)
  instructions(0x16) = LD(D,Immediate8)
  instructions(0x26) = LD(H,Immediate8)
  instructions(0x0E) = LD(C,Immediate8)
  instructions(0x1E) = LD(E,Immediate8)
  instructions(0x2E) = LD(L,Immediate8)
  instructions(0x3E) = LD(A,Immediate8)

  // LD B,X
  instructions(0x40) = LD(B,B)
  instructions(0x41) = LD(B,C)
  instructions(0x42) = LD(B,D)
  instructions(0x43) = LD(B,E)
  instructions(0x44) = LD(B,H)
  instructions(0x45) = LD(B,L)
  instructions(0x47) = LD(B,A)

  // LD C,X
  instructions(0x48) = LD(C,B)
  instructions(0x49) = LD(C,C)
  instructions(0x4A) = LD(C,D)
  instructions(0x4B) = LD(C,E)
  instructions(0x4C) = LD(C,H)
  instructions(0x4D) = LD(C,L)
  instructions(0x4F) = LD(C,A)

  // LD D,X
  instructions(0x50) = LD(D,B)
  instructions(0x51) = LD(D,C)
  instructions(0x52) = LD(D,D)
  instructions(0x53) = LD(D,E)
  instructions(0x54) = LD(D,H)
  instructions(0x55) = LD(D,L)
  instructions(0x57) = LD(D,A)

  // LD E,X
  instructions(0x58) = LD(E,B)
  instructions(0x59) = LD(E,C)
  instructions(0x5A) = LD(E,D)
  instructions(0x5B) = LD(E,E)
  instructions(0x5C) = LD(E,H)
  instructions(0x5D) = LD(E,L)
  instructions(0x5F) = LD(E,A)

  // LD H,X
  instructions(0x60) = LD(H,B)
  instructions(0x61) = LD(H,C)
  instructions(0x62) = LD(H,D)
  instructions(0x63) = LD(H,E)
  instructions(0x64) = LD(H,H)
  instructions(0x65) = LD(H,L)
  instructions(0x67) = LD(H,A)

  // LD L,X
  instructions(0x68) = LD(L,B)
  instructions(0x69) = LD(L,C)
  instructions(0x6A) = LD(L,D)
  instructions(0x6B) = LD(L,E)
  instructions(0x6C) = LD(L,H)
  instructions(0x6D) = LD(L,L)
  instructions(0x6F) = LD(L,A)

  // LD A,X
  instructions(0x78) = LD(A,B)
  instructions(0x79) = LD(A,C)
  instructions(0x7A) = LD(A,D)
  instructions(0x7B) = LD(A,E)
  instructions(0x7C) = LD(A,H)
  instructions(0x7D) = LD(A,L)
  instructions(0x7F) = LD(A,A)

  // LD (XX),Y
  instructions(0x02) = LD(Memory8(BC), A)
  instructions(0x12) = LD(Memory8(DE), A)
  instructions(0x70) = LD(Memory8(HL),B)
  instructions(0x71) = LD(Memory8(HL),C)
  instructions(0x72) = LD(Memory8(HL),D)
  instructions(0x73) = LD(Memory8(HL),E)
  instructions(0x74) = LD(Memory8(HL),H)
  instructions(0x75) = LD(Memory8(HL),L)
  instructions(0x77) = LD(Memory8(HL),A)

  // LD X,(YY)
  instructions(0x46) = LD(B, Memory8(HL))
  instructions(0x56) = LD(D, Memory8(HL))
  instructions(0x66) = LD(H, Memory8(HL))
  instructions(0x4E) = LD(C, Memory8(HL))
  instructions(0x5E) = LD(E, Memory8(HL))
  instructions(0x6E) = LD(L, Memory8(HL))
  instructions(0x7E) = LD(A, Memory8(HL))

  // LD (a16),x
  instructions(0xEA) = LD(Memory8(Immediate16), A)

  // LD x,(a16)
  instructions(0xFA) = LD(A,Memory8(Immediate16))

  // LDH (a8),A
  instructions(0xE0) = LD(Memory8(ZeroPage),A)

  // LDH A, (a8
  instructions(0xF0) = LD(A,Memory8(ZeroPage))

  // ADD A,X
  instructions(0x80) = ADD(A,B)
  instructions(0x81) = ADD(A,C)
  instructions(0x82) = ADD(A,D)
  instructions(0x83) = ADD(A,E)
  instructions(0x84) = ADD(A,H)
  instructions(0x85) = ADD(A,L)
  instructions(0x86) = ADD(A,Memory8(HL))
  instructions(0x87) = ADD(A,A)

  // ADC A,X
  instructions(0x88) = ADC(A,B)
  instructions(0x89) = ADC(A,C)
  instructions(0x8A) = ADC(A,D)
  instructions(0x8B) = ADC(A,E)
  instructions(0x8C) = ADC(A,H)
  instructions(0x8D) = ADC(A,L)
  instructions(0x8E) = ADC(A,Memory8(HL))
  instructions(0x8F) = ADC(A,A)

  // SUB X
  instructions(0x90) = SUB(A,B)
  instructions(0x91) = SUB(A,C)
  instructions(0x92) = SUB(A,D)
  instructions(0x93) = SUB(A,E)
  instructions(0x94) = SUB(A,H)
  instructions(0x95) = SUB(A,L)
  instructions(0x96) = SUB(A,Memory8(HL))
  instructions(0x97) = SUB(A,A)

  // SBC A,X
  instructions(0x98) = SBC(A,B)
  instructions(0x99) = SBC(A,C)
  instructions(0x9A) = SBC(A,D)
  instructions(0x9B) = SBC(A,E)
  instructions(0x9C) = SBC(A,H)
  instructions(0x9D) = SBC(A,L)
  instructions(0x9E) = SBC(A,Memory8(HL))
  instructions(0x9F) = SBC(A,A)

  // AND X
  instructions(0xA0) = AND(A,B)
  instructions(0xA1) = AND(A,C)
  instructions(0xA3) = AND(A,D)
  instructions(0xA3) = AND(A,E)
  instructions(0xA4) = AND(A,H)
  instructions(0xA5) = AND(A,L)
  instructions(0xA6) = AND(A,Memory8(HL))
  instructions(0xA7) = AND(A,A)

  // XOR X
  instructions(0xA8) = XOR(A,B)
  instructions(0xA9) = XOR(A,C)
  instructions(0xAA) = XOR(A,D)
  instructions(0xAB) = XOR(A,E)
  instructions(0xAC) = XOR(A,H)
  instructions(0xAD) = XOR(A,L)
  instructions(0xAE) = XOR(A,Memory8(HL))
  instructions(0xAF) = XOR(A,A)

  // OR X
  instructions(0xB0) = OR(A,B)
  instructions(0xB1) = OR(A,C)
  instructions(0xB3) = OR(A,D)
  instructions(0xB3) = OR(A,E)
  instructions(0xB4) = OR(A,H)
  instructions(0xB5) = OR(A,L)
  instructions(0xB6) = OR(A,Memory8(HL))
  instructions(0xB7) = OR(A,A)

  // CP X
  instructions(0xB8) = CP(A,B)
  instructions(0xB9) = CP(A,C)
  instructions(0xBA) = CP(A,D)
  instructions(0xBB) = CP(A,E)
  instructions(0xBC) = CP(A,H)
  instructions(0xBD) = CP(A,L)
  instructions(0xBE) = CP(A,Memory8(HL))
  instructions(0xBF) = CP(A,A)

  // JP
  instructions(0xC2) = JP(!_.Flags.Z.isSet)
  instructions(0xC3) = JP(_ => true)
  instructions(0xCA) = JP(_.Flags.Z.isSet)
  instructions(0xD2) = JP(!_.Flags.C.isSet)
  instructions(0xDA) = JP(_.Flags.C.isSet)

  // Interrupts (Not Implemented)
  instructions(0xF3) = NOP // DI
  instructions(0xFB) = NOP // EI
}
