package com.jhood.sgbc.cli

import java.io.File

import com.jhood.sgbc.lr35902.CPU
import com.jhood.sgbc.memory.{DumbMemoryBlob, MappedMemoryController}
import com.jhood.sgbc.rom.FileROM
import com.jhood.sgbc.serial.ConsoleSerialEmitter

object CLI {
  def main(args: Array[String]): Unit = {
    val memory = new MappedMemoryController(List(
      new ConsoleSerialEmitter,
      new DumbMemoryBlob
    ))

    val rom = new FileROM(new File("cpu_instrs.gb"))
    rom.load.zipWithIndex.foreach { case (b,addr) =>
      memory.write(addr.toShort,b)
    }

    val cpu = new CPU(memory)
    while(true) {
      cpu.tick
    }
  }
}
