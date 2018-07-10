package com.jhood.sgbc.cpu.instructions.prefix

import com.jhood.sgbc.cpu.{CPU, Immediate8}
import com.jhood.sgbc.cpu.instructions.{ImplementedInstruction, PrefixTable}

object PREFIX extends ImplementedInstruction {
  override def name: String = "PREFIX"
  override def cycles: Int = 4
  override def execute(cpu: CPU): Unit = {
    val prefixCode = cpu.read(Immediate8)
    PrefixTable.instructions(prefixCode) match {
      case inst: ImplementedInstruction =>
        //println(s"  => ${inst.name}")
        inst.execute(cpu)
      case _ =>
        throw new Exception(s"Prefix instruction not implemented 0x${prefixCode.toHexString}")
    }
  }
}
