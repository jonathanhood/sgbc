package com.jhood.sgbc.lr35902.instructions

import com.jhood.sgbc.lr35902.CPU

abstract class Instruction
object NotImplementedInstruction extends Instruction

abstract class ImplementedInstruction extends Instruction {
  def name: String
  def cycles: Int
  def width: Int
  def execute(cpu: CPU): Unit
}
