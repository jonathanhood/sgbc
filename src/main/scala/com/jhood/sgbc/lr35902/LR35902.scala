package com.jhood.sgbc.lr35902

import com.jhood.sgbc.lr35902.instructions.{ImplementedInstruction, Instruction, NotImplementedInstruction}
import com.jhood.sgbc.lr35902.instructions.alu._
import com.jhood.sgbc.lr35902.instructions.load.{LD, LD16, LDD, LDI}
import com.jhood.sgbc.memory.MemoryController



class LR35902(registers: Registers, memory: MemoryController) {

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
