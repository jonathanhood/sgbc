package com.jhood.sgbc.lr35902.instructions.alu

import com.jhood.sgbc.lr35902.instructions.ImplementedInstruction
import com.jhood.sgbc.lr35902.{Memory8, Operand8}


abstract class ALUInstruction extends ImplementedInstruction {
  def left: Operand8
  def right: Operand8

  override final val cycles: Int = right match {
    case _ : Memory8  => 8
    case _ : Operand8 => 4
  }

  override final val width: Int = 1
}
