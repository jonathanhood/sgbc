package com.jhood.sgbc.lr35902.instructions.alu

import com.jhood.sgbc.lr35902.{Operand8, Registers}
import com.jhood.sgbc.memory.MemoryController


case class CP(left: Operand8, right: Operand8) extends ALUInstruction {
  override def name: String = s"CP ${left.name}, ${right.name}"
  override def execute(registers: Registers, memory: MemoryController): Unit = {
    // Comparison on the lr35902 looks to be a subtraction where flags are
    // set but the result is not retained
    registers.ALU.Oper8(left.get,right.get,_ - _)
    registers.Flags.N.set(true)
  }
}
