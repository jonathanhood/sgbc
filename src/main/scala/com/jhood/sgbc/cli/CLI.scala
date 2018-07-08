package com.jhood.sgbc.cli

import java.io.File

import com.jhood.sgbc.cpu.CPU
import com.jhood.sgbc.memory.{DumbMemoryBlob, MappedMemoryController}
import com.jhood.sgbc.rom.FileROM
import com.jhood.sgbc.serial.BufferedSerialEmitter

object CLI {
  def main(args: Array[String]): Unit = {
    val memory = new MappedMemoryController(List(
      new BufferedSerialEmitter,
      new DumbMemoryBlob
    ))

    val rom = new FileROM(new File("06-ld r,r.gb"))
    rom.load.zipWithIndex.foreach { case (b,addr) =>
      memory.write(addr.toShort,b)
    }

    val cpu = new CPU(memory)
    while(true) {
      cpu.tick
    }
  }
}
