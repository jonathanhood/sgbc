package com.jhood.sgbc.cpu.instructions

import com.jhood.sgbc.cpu._
import com.jhood.sgbc.cpu.instructions.alu._
import com.jhood.sgbc.cpu.instructions.flow._
import com.jhood.sgbc.cpu.instructions.load._
import com.jhood.sgbc.cpu.instructions.misc.{CCF, NOP, SCF}
import com.jhood.sgbc.cpu.instructions.prefix.{PREFIX, RLC, RR, RRC}

object InstructionTable {
  val instructions: Array[Instruction] = Array.fill(0x1FF)(NotImplementedInstruction)

  // Misc
  instructions(0x00) = NOP
  instructions(0x10) = STOP
  instructions(0xCB) = PREFIX
  instructions(0x0F) = RRC(A)
  instructions(0x1F) = RR(A)
  instructions(0x37) = SCF
  instructions(0x3F) = CCF
  instructions(0x07) = RLC(A)
  instructions(0x2F) = CPL

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
  instructions(0x36) = LD(Memory8(HL),Immediate8)

  // LD X,(YY)
  instructions(0x0A) = LD(A, Memory8(BC))
  instructions(0x1A) = LD(A, Memory8(DE))
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
  instructions(0xE0) = LD(Memory8(ZeroPage(Immediate8)),A)

  // LDH A, (a8)
  instructions(0xF0) = LD(A,Memory8(ZeroPage(Immediate8)))

  // LD (C), A
  instructions(0xE2) = LD(Memory8(ZeroPage(C)),A)

  // LC A, (C)
  instructions(0xF2) = LD(A,Memory8(ZeroPage(C)))

  // LD HL w/ Modify
  instructions(0x22) = LD(Memory8(HLI),A)
  instructions(0x32) = LD(Memory8(HLD),A)
  instructions(0x2A) = LD(A,Memory8(HLI))
  instructions(0x3A) = LD(A,Memory8(HLD))

  // LD HL
  instructions(0xF8) = LDAddImmediate(HL,SP)
  instructions(0xF9) = LD16(SP,HL)

  // LD (a16), SP
  instructions(0x08) = LD16(Memory16(Immediate16), SP)

  // ADD A,X
  instructions(0x80) = ADD(A,B)
  instructions(0x81) = ADD(A,C)
  instructions(0x82) = ADD(A,D)
  instructions(0x83) = ADD(A,E)
  instructions(0x84) = ADD(A,H)
  instructions(0x85) = ADD(A,L)
  instructions(0x86) = ADD(A,Memory8(HL))
  instructions(0x87) = ADD(A,A)
  instructions(0xC6) = ADD(A,Immediate8)
  instructions(0x09) = ADD16(HL,BC, false)
  instructions(0x19) = ADD16(HL,DE, false)
  instructions(0x29) = ADD16(HL,HL, false)
  instructions(0x39) = ADD16(HL,SP, false)
  instructions(0xE8) = ADD16(SP,PaddedImmediate8, true)

  // ADC A,X
  instructions(0x88) = ADC(A,B)
  instructions(0x89) = ADC(A,C)
  instructions(0x8A) = ADC(A,D)
  instructions(0x8B) = ADC(A,E)
  instructions(0x8C) = ADC(A,H)
  instructions(0x8D) = ADC(A,L)
  instructions(0x8E) = ADC(A,Memory8(HL))
  instructions(0x8F) = ADC(A,A)
  instructions(0xCE) = ADC(A,Immediate8)

  // SUB X
  instructions(0x90) = SUB(A,B)
  instructions(0x91) = SUB(A,C)
  instructions(0x92) = SUB(A,D)
  instructions(0x93) = SUB(A,E)
  instructions(0x94) = SUB(A,H)
  instructions(0x95) = SUB(A,L)
  instructions(0x96) = SUB(A,Memory8(HL))
  instructions(0x97) = SUB(A,A)
  instructions(0xD6) = SUB(A,Immediate8)

  // SBC A,X
  instructions(0x98) = SBC(A,B)
  instructions(0x99) = SBC(A,C)
  instructions(0x9A) = SBC(A,D)
  instructions(0x9B) = SBC(A,E)
  instructions(0x9C) = SBC(A,H)
  instructions(0x9D) = SBC(A,L)
  instructions(0x9E) = SBC(A,Memory8(HL))
  instructions(0x9F) = SBC(A,A)
  instructions(0xDE) = SBC(A,Immediate8)

  // AND X
  instructions(0xA0) = AND(A,B)
  instructions(0xA1) = AND(A,C)
  instructions(0xA2) = AND(A,D)
  instructions(0xA3) = AND(A,E)
  instructions(0xA4) = AND(A,H)
  instructions(0xA5) = AND(A,L)
  instructions(0xA6) = AND(A,Memory8(HL))
  instructions(0xA7) = AND(A,A)
  instructions(0xE6) = AND(A,Immediate8)

  // XOR X
  instructions(0xA8) = XOR(A,B)
  instructions(0xA9) = XOR(A,C)
  instructions(0xAA) = XOR(A,D)
  instructions(0xAB) = XOR(A,E)
  instructions(0xAC) = XOR(A,H)
  instructions(0xAD) = XOR(A,L)
  instructions(0xAE) = XOR(A,Memory8(HL))
  instructions(0xAF) = XOR(A,A)
  instructions(0xEE) = XOR(A,Immediate8)

  // OR X
  instructions(0xB0) = OR(A,B)
  instructions(0xB1) = OR(A,C)
  instructions(0xB2) = OR(A,D)
  instructions(0xB3) = OR(A,E)
  instructions(0xB4) = OR(A,H)
  instructions(0xB5) = OR(A,L)
  instructions(0xB6) = OR(A,Memory8(HL))
  instructions(0xB7) = OR(A,A)
  instructions(0xF6) = OR(A,Immediate8)

  // CP X
  instructions(0xB8) = CP(A,B)
  instructions(0xB9) = CP(A,C)
  instructions(0xBA) = CP(A,D)
  instructions(0xBB) = CP(A,E)
  instructions(0xBC) = CP(A,H)
  instructions(0xBD) = CP(A,L)
  instructions(0xBE) = CP(A,Memory8(HL))
  instructions(0xBF) = CP(A,A)
  instructions(0xFE) = CP(A,Immediate8)

  // INC X
  instructions(0x04) = INC(B)
  instructions(0x14) = INC(D)
  instructions(0x24) = INC(H)
  instructions(0x34) = INC(Memory8(HL))
  instructions(0x0C) = INC(C)
  instructions(0x1C) = INC(E)
  instructions(0x2C) = INC(L)
  instructions(0x3C) = INC(A)
  instructions(0x03) = INC16(BC)
  instructions(0x13) = INC16(DE)
  instructions(0x23) = INC16(HL)
  instructions(0x33) = INC16(SP)

  // DEC X
  instructions(0x05) = DEC(B)
  instructions(0x15) = DEC(D)
  instructions(0x25) = DEC(H)
  instructions(0x35) = DEC(Memory8(HL))
  instructions(0x0D) = DEC(C)
  instructions(0x1D) = DEC(E)
  instructions(0x2D) = DEC(L)
  instructions(0x3D) = DEC(A)
  instructions(0x0B) = DEC16(BC)
  instructions(0x1B) = DEC16(DE)
  instructions(0x2B) = DEC16(HL)
  instructions(0x3B) = DEC16(SP)

  // JP
  instructions(0xC2) = JP(Immediate16, "JP NZ,a16",!_.Flags.Z.isSet)
  instructions(0xC3) = JP(Immediate16, "JP a16",_ => true)
  instructions(0xCA) = JP(Immediate16, "JP Z,a16",_.Flags.Z.isSet)
  instructions(0xD2) = JP(Immediate16, "JP C,a16",!_.Flags.C.isSet)
  instructions(0xDA) = JP(Immediate16, "JP NC,a16",_.Flags.C.isSet)
  instructions(0xE9) = JP(HL, "JP (HL)", _ => true)

  // JR
  instructions(0x20) = JR("JR NZ,r8", !_.Flags.Z.isSet)
  instructions(0x30) = JR("JR NC,r8", !_.Flags.C.isSet)
  instructions(0x18) = JR("JR r8", _ => true)
  instructions(0x28) = JR("JR Z,r8", _.Flags.Z.isSet)
  instructions(0x38) = JR("JR C,r8",_.Flags.C.isSet)

  // CALL
  instructions(0xC4) = CALL("CALL NZ,a16",!_.Flags.Z.isSet)
  instructions(0xD4) = CALL("CALL NC,a16",!_.Flags.C.isSet)
  instructions(0xCC) = CALL("CALL Z,a16",_.Flags.Z.isSet)
  instructions(0xDC) = CALL("CALL C,a16",_.Flags.C.isSet)
  instructions(0xCD) = CALL("CALL a16", _ => true)

  // RET
  instructions(0xC0) = RET("RET NZ", !_.Flags.Z.isSet)
  instructions(0xD0) = RET("RET NC", !_.Flags.C.isSet)
  instructions(0xC8) = RET("RET Z", _.Flags.Z.isSet)
  instructions(0xD8) = RET("RET C", _.Flags.C.isSet)
  instructions(0xC9) = RET("RET", _ => true)

  // RETI (interrupts aren't supported for now)
  instructions(0xD9) = RET("RETI",_ => true)

  // RST
  instructions(0xC7) = RST(0x00)
  instructions(0xD7) = RST(0x10)
  instructions(0xE7) = RST(0x20)
  instructions(0xF7) = RST(0x30)
  instructions(0xCF) = RST(0x08)
  instructions(0xDF) = RST(0x18)
  instructions(0xEF) = RST(0x28)
  instructions(0xFF) = RST(0x38)

  // PUSH
  instructions(0xC5) = PUSH(BC)
  instructions(0xD5) = PUSH(DE)
  instructions(0xE5) = PUSH(HL)
  instructions(0xF5) = PUSH(AF)

  // POP
  instructions(0xC1) = POP(BC)
  instructions(0xD1) = POP(DE)
  instructions(0xE1) = POP(HL)
  instructions(0xF1) = POP(AF)

  // Interrupts (Not Implemented)
  instructions(0xF3) = DI
  instructions(0xFB) = NOP // EI
}
