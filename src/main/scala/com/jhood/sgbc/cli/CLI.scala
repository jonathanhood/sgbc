package com.jhood.sgbc.cli

import java.io.{File, PrintWriter}

import com.jhood.sgbc.lr35902.instructions.{ImplementedInstruction, InstructionTable}

object CLI {
  def main(args: Array[String]): Unit = {
    val writer = new PrintWriter(new File("instructions.csv"))
    writer.println("sep=;")
    writer.println("opcode;instruction;width;cycles")
    InstructionTable.instructions.zipWithIndex collect {
      case (inst : ImplementedInstruction, opcode) =>
        writer.println(s"0x${opcode.toHexString};${inst.name};${inst.width};${inst.cycles}")
    }
    writer.close()
  }
}
