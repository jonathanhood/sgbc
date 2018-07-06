package com.jhood.sgbc.lr35902.instructions.alu

import com.jhood.sgbc.lr35902.{Operand8, Registers}
import com.jhood.sgbc.memory.MemoryController

case class SUB(left: Operand8, right: Operand8) extends ALUInstruction {
  // left = left - right
  override def name: String = s"SUB ${left.name}, ${right.name}"
  override def execute(registers: Registers, memory: MemoryController): Unit = {
    val withCarry = (right.get + registers.Flags.C.get).toByte
    val result = registers.ALU.Oper8(left.get,withCarry, _ - _)
    registers.Flags.N.set(true)
    left.write(result)
  }
}
