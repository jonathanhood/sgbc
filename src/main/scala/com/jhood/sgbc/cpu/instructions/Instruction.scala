package com.jhood.sgbc.cpu.instructions

import com.jhood.sgbc.cpu.CPU

abstract class Instruction
object NotImplementedInstruction extends Instruction

abstract class ImplementedInstruction extends Instruction {
  def name: String
  def cycles: Int
  def width: Int
  def execute(cpu: CPU): Unit
}
