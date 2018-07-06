package com.jhood.sgbc.cli

import java.io.{File, PrintWriter}

import com.jhood.sgbc.lr35902.instructions.ImplementedInstruction
import com.jhood.sgbc.lr35902.{LR35902, Registers}
import com.jhood.sgbc.memory.MemoryController

object CLI {
  def main(args: Array[String]): Unit = {
    val registers = new Registers
    val memory = new MemoryController {
      override def fetch(address: Short): Byte = ???
      override def fetchShort(address: Short): Short = ???
      override def write(address: Short, value: Byte): Unit = ???
      override def writeShort(address: Short, value: Short): Unit = ???
    }
    val cpu = new LR35902(registers, memory)

    val writer = new PrintWriter(new File("instructions.csv"))
    writer.println("sep=;")
    writer.println("opcode;instruction;width;cycles")
    cpu.instructions.zipWithIndex collect {
      case (inst : ImplementedInstruction, opcode) =>
        writer.println(s"0x${opcode.toHexString};${inst.name};${inst.width};${inst.cycles}")
    }
    writer.close()
  }
}
