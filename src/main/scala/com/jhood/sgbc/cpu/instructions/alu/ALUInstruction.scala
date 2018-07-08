package com.jhood.sgbc.cpu.instructions.alu

import com.jhood.sgbc.cpu.instructions.ImplementedInstruction
import com.jhood.sgbc.cpu.{Immediate8, Memory8, Operand8}


abstract class ALUInstruction extends ImplementedInstruction {
  def left: Operand8
  def right: Operand8

  override final val cycles: Int = (left,right) match {
    case (_ : Memory8, _ : Memory8)   => 12
    case (_ : Memory8, _)             => 8
    case (_, _ : Memory8)             => 8
    case (_, Immediate8)              => 8
    case (_, _ )                      => 4
  }
}
