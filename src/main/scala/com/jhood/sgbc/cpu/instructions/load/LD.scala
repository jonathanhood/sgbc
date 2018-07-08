package com.jhood.sgbc.cpu.instructions.load

import com.jhood.sgbc.cpu._
import com.jhood.sgbc.cpu.instructions.ImplementedInstruction

case class LD(target: Operand8, source: Operand8) extends ImplementedInstruction {
  override final val name: String = s"LD ${target.name},${source.name}"
  override final val cycles: Int = (source,target) match {
    case (_,Memory8(Immediate16)) => 16
    case (Memory8(Immediate16),_) => 16
    case (_,Memory8(ZeroPage))    => 12
    case (Memory8(ZeroPage),_)    => 12
    case (_ : Register8,_)        => 4
    case (_ : Memory8,_)          => 8
    case (Immediate8,_)           => 8
  }
  override final val width: Int = (source,target) match {
    case (Immediate8,_)           => 2
    case (Memory8(ZeroPage),_)    => 2
    case (_,Memory8(ZeroPage))    => 2
    case (Memory8(Immediate16),_) => 3
    case (_,Memory8(Immediate16)) => 3
    case _                        => 1
  }
  override def execute(cpu: CPU): Unit = {
    cpu.write(target, cpu.read(source))
    mutateOperand(cpu, target)
    mutateOperand(cpu, source)
    cpu.incrementPC(this)
  }

  def mutateOperand(cpu: CPU, target: Operand8): Unit = target match {
    case Memory8(HLD) => cpu.Registers.decrement(HL)
    case Memory8(HLI) => cpu.Registers.increment(HL)
    case _ =>
  }
}
