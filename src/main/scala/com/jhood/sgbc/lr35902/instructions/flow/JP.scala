package com.jhood.sgbc.lr35902.instructions.flow

import com.jhood.sgbc.lr35902.{CPU, Immediate16, PC}
import com.jhood.sgbc.lr35902.instructions.ImplementedInstruction

case class JP(condition: (CPU) => Boolean) extends ImplementedInstruction {
  override def name: String = "JP"

  override def cycles: Int = 16
  override def width: Int = 3
  override def execute(cpu: CPU): Unit = {
    val addr = cpu.read(Immediate16)
    if(condition(cpu)) {
      println(s"JUMPING ${addr.toHexString}")
      cpu.Registers.write(PC,addr)
    } else {
      cpu.incrementPC(this)
    }
  }
}
