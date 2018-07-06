package com.jhood.sgbc.lr35902.instructions

import com.jhood.sgbc.lr35902.{Memory16, Memory8, Registers}
import com.jhood.sgbc.lr35902.instructions.alu._
import com.jhood.sgbc.lr35902.instructions.load.{LD, LD16, LDD, LDI}
import com.jhood.sgbc.memory.MemoryController

object InstructionTable {
  def generate(registers: Registers, memory: MemoryController): Array[Instruction] = {
    val instructions: Array[Instruction] = Array.fill(0xFF)(NotImplementedInstruction)

    // LD XX,d16
    instructions(0x01) = LD16(registers.BC,Memory16(registers.PC, memory, 1))
    instructions(0x11) = LD16(registers.DE,Memory16(registers.PC, memory, 1))
    instructions(0x21) = LD16(registers.HL,Memory16(registers.PC, memory, 1))
    instructions(0x31) = LD16(registers.SP,Memory16(registers.PC, memory, 1))

    // LD X,d8
    instructions(0x06) = LD(registers.B,Memory8(registers.PC, memory, 1))
    instructions(0x16) = LD(registers.D,Memory8(registers.PC, memory, 1))
    instructions(0x26) = LD(registers.H,Memory8(registers.PC, memory, 1))
    instructions(0x0E) = LD(registers.C,Memory8(registers.PC, memory, 1))
    instructions(0x1E) = LD(registers.E,Memory8(registers.PC, memory, 1))
    instructions(0x2E) = LD(registers.L,Memory8(registers.PC, memory, 1))
    instructions(0x3E) = LD(registers.A,Memory8(registers.PC, memory, 1))

    // LD B,X
    instructions(0x40) = LD(registers.B,registers.B)
    instructions(0x41) = LD(registers.B,registers.C)
    instructions(0x42) = LD(registers.B,registers.D)
    instructions(0x43) = LD(registers.B,registers.E)
    instructions(0x44) = LD(registers.B,registers.H)
    instructions(0x45) = LD(registers.B,registers.L)
    instructions(0x47) = LD(registers.B,registers.A)

    // LD C,X
    instructions(0x48) = LD(registers.C,registers.B)
    instructions(0x49) = LD(registers.C,registers.C)
    instructions(0x4A) = LD(registers.C,registers.D)
    instructions(0x4B) = LD(registers.C,registers.E)
    instructions(0x4C) = LD(registers.C,registers.H)
    instructions(0x4D) = LD(registers.C,registers.L)
    instructions(0x4F) = LD(registers.C,registers.A)

    // LD D,X
    instructions(0x50) = LD(registers.D,registers.B)
    instructions(0x51) = LD(registers.D,registers.C)
    instructions(0x52) = LD(registers.D,registers.D)
    instructions(0x53) = LD(registers.D,registers.E)
    instructions(0x54) = LD(registers.D,registers.H)
    instructions(0x55) = LD(registers.D,registers.L)
    instructions(0x57) = LD(registers.D,registers.A)

    // LD E,X
    instructions(0x58) = LD(registers.E,registers.B)
    instructions(0x59) = LD(registers.E,registers.C)
    instructions(0x5A) = LD(registers.E,registers.D)
    instructions(0x5B) = LD(registers.E,registers.E)
    instructions(0x5C) = LD(registers.E,registers.H)
    instructions(0x5D) = LD(registers.E,registers.L)
    instructions(0x5F) = LD(registers.E,registers.A)

    // LD H,X
    instructions(0x60) = LD(registers.H,registers.B)
    instructions(0x61) = LD(registers.H,registers.C)
    instructions(0x62) = LD(registers.H,registers.D)
    instructions(0x63) = LD(registers.H,registers.E)
    instructions(0x64) = LD(registers.H,registers.H)
    instructions(0x65) = LD(registers.H,registers.L)
    instructions(0x67) = LD(registers.H,registers.A)

    // LD L,X
    instructions(0x68) = LD(registers.L,registers.B)
    instructions(0x69) = LD(registers.L,registers.C)
    instructions(0x6A) = LD(registers.L,registers.D)
    instructions(0x6B) = LD(registers.L,registers.E)
    instructions(0x6C) = LD(registers.L,registers.H)
    instructions(0x6D) = LD(registers.L,registers.L)
    instructions(0x6F) = LD(registers.L,registers.A)

    // LD A,X
    instructions(0x78) = LD(registers.A,registers.B)
    instructions(0x79) = LD(registers.A,registers.C)
    instructions(0x7A) = LD(registers.A,registers.D)
    instructions(0x7B) = LD(registers.A,registers.E)
    instructions(0x7C) = LD(registers.A,registers.H)
    instructions(0x7D) = LD(registers.A,registers.L)
    instructions(0x7F) = LD(registers.A,registers.A)

    // LD (XX),Y
    instructions(0x02) = LD(Memory8(registers.BC,memory), registers.A)
    instructions(0x12) = LD(Memory8(registers.DE,memory), registers.A)
    instructions(0x22) = LDI // LD (HL+), A
    instructions(0x32) = LDD // LD (HL-), A
    instructions(0x70) = LD(Memory8(registers.HL,memory),registers.B)
    instructions(0x71) = LD(Memory8(registers.HL,memory),registers.C)
    instructions(0x72) = LD(Memory8(registers.HL,memory),registers.D)
    instructions(0x73) = LD(Memory8(registers.HL,memory),registers.E)
    instructions(0x74) = LD(Memory8(registers.HL,memory),registers.H)
    instructions(0x75) = LD(Memory8(registers.HL,memory),registers.L)
    instructions(0x77) = LD(Memory8(registers.HL,memory),registers.A)

    // LD X,(YY)
    instructions(0x46) = LD(registers.B, Memory8(registers.HL,memory))
    instructions(0x56) = LD(registers.D, Memory8(registers.HL,memory))
    instructions(0x66) = LD(registers.H, Memory8(registers.HL,memory))
    instructions(0x4E) = LD(registers.C, Memory8(registers.HL,memory))
    instructions(0x5E) = LD(registers.E, Memory8(registers.HL,memory))
    instructions(0x6E) = LD(registers.L, Memory8(registers.HL,memory))
    instructions(0x7E) = LD(registers.A, Memory8(registers.HL,memory))

    // ADD A,X
    instructions(0x80) = ADD(registers.A,registers.B)
    instructions(0x81) = ADD(registers.A,registers.C)
    instructions(0x82) = ADD(registers.A,registers.D)
    instructions(0x83) = ADD(registers.A,registers.E)
    instructions(0x84) = ADD(registers.A,registers.H)
    instructions(0x85) = ADD(registers.A,registers.L)
    instructions(0x86) = ADD(registers.A,Memory8(registers.HL,memory))
    instructions(0x87) = ADD(registers.A,registers.A)

    // ADC A,X
    instructions(0x88) = ADC(registers.A,registers.B)
    instructions(0x89) = ADC(registers.A,registers.C)
    instructions(0x8A) = ADC(registers.A,registers.D)
    instructions(0x8B) = ADC(registers.A,registers.E)
    instructions(0x8C) = ADC(registers.A,registers.H)
    instructions(0x8D) = ADC(registers.A,registers.L)
    instructions(0x8E) = ADC(registers.A,Memory8(registers.HL,memory))
    instructions(0x8F) = ADC(registers.A,registers.A)

    // SUB X
    instructions(0x90) = SUB(registers.A,registers.B)
    instructions(0x91) = SUB(registers.A,registers.C)
    instructions(0x92) = SUB(registers.A,registers.D)
    instructions(0x93) = SUB(registers.A,registers.E)
    instructions(0x94) = SUB(registers.A,registers.H)
    instructions(0x95) = SUB(registers.A,registers.L)
    instructions(0x96) = SUB(registers.A,Memory8(registers.HL,memory))
    instructions(0x97) = SUB(registers.A,registers.A)

    // SBC A,X
    instructions(0x98) = SBC(registers.A,registers.B)
    instructions(0x99) = SBC(registers.A,registers.C)
    instructions(0x9A) = SBC(registers.A,registers.D)
    instructions(0x9B) = SBC(registers.A,registers.E)
    instructions(0x9C) = SBC(registers.A,registers.H)
    instructions(0x9D) = SBC(registers.A,registers.L)
    instructions(0x9E) = SBC(registers.A,Memory8(registers.HL,memory))
    instructions(0x9F) = SBC(registers.A,registers.A)

    // AND X
    instructions(0xA0) = AND(registers.A,registers.B)
    instructions(0xA1) = AND(registers.A,registers.C)
    instructions(0xA3) = AND(registers.A,registers.D)
    instructions(0xA3) = AND(registers.A,registers.E)
    instructions(0xA4) = AND(registers.A,registers.H)
    instructions(0xA5) = AND(registers.A,registers.L)
    instructions(0xA6) = AND(registers.A,Memory8(registers.HL,memory))
    instructions(0xA7) = AND(registers.A,registers.A)

    // XOR X
    instructions(0xA8) = XOR(registers.A,registers.B)
    instructions(0xA9) = XOR(registers.A,registers.C)
    instructions(0xAA) = XOR(registers.A,registers.D)
    instructions(0xAB) = XOR(registers.A,registers.E)
    instructions(0xAC) = XOR(registers.A,registers.H)
    instructions(0xAD) = XOR(registers.A,registers.L)
    instructions(0xAE) = XOR(registers.A,Memory8(registers.HL,memory))
    instructions(0xAF) = XOR(registers.A,registers.A)

    // OR X
    instructions(0xB0) = OR(registers.A,registers.B)
    instructions(0xB1) = OR(registers.A,registers.C)
    instructions(0xB3) = OR(registers.A,registers.D)
    instructions(0xB3) = OR(registers.A,registers.E)
    instructions(0xB4) = OR(registers.A,registers.H)
    instructions(0xB5) = OR(registers.A,registers.L)
    instructions(0xB6) = OR(registers.A,Memory8(registers.HL,memory))
    instructions(0xB7) = OR(registers.A,registers.A)

    // CP X
    instructions(0xB8) = CP(registers.A,registers.B)
    instructions(0xB9) = CP(registers.A,registers.C)
    instructions(0xBA) = CP(registers.A,registers.D)
    instructions(0xBB) = CP(registers.A,registers.E)
    instructions(0xBC) = CP(registers.A,registers.H)
    instructions(0xBD) = CP(registers.A,registers.L)
    instructions(0xBE) = CP(registers.A,Memory8(registers.HL,memory))
    instructions(0xBF) = CP(registers.A,registers.A)

    instructions
  }
}
