package com.jhood.sgbc.lr35902.instructions.load

import com.jhood.sgbc.lr35902.Registers
import com.jhood.sgbc.lr35902.instructions.ImplementedInstruction
import com.jhood.sgbc.memory.MemoryController

object LDD extends ImplementedInstruction {
  override def name: String = s"LD (HL-),A"
  override def cycles: Int = 8
  override def width: Int = 1
  override def execute(registers: Registers, memory: MemoryController): Unit =  {
    registers.A.write(memory.fetch(registers.HL.get))
    registers.HL.increment(-1)
  }
}
