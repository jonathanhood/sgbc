package com.jhood.sgbc.lr35902

import com.jhood.sgbc.lr35902.instructions.{ImplementedInstruction, InstructionTable, NotImplementedInstruction}
import com.jhood.sgbc.memory.MemoryController



class LR35902(registers: Registers, memory: MemoryController) {
  val instructions = InstructionTable.generate(registers, memory)

  def tick: Unit = {
    instructions(memory.fetch(registers.PC.get)) match {
      case inst : ImplementedInstruction =>
        inst.execute(registers, memory)
        registers.PC.increment(inst.width)
      case NotImplementedInstruction =>
        throw new Exception("Instruction not implemented")
    }
  }
}
