package integration

import java.io.File

import com.jhood.sgbc.cpu.CPU
import com.jhood.sgbc.memory.{DumbMemoryBlob, MappedMemoryController}
import com.jhood.sgbc.rom.FileROM
import com.jhood.sgbc.serial.BufferedSerialEmitter
import org.scalatest.FlatSpec

//class LDTests extends ROMTestExecutor(new File("./src/test/roms/06-ld r,r.gb"))

abstract class ROMTestExecutor(romFile: File) extends FlatSpec  {
  "A GameBoy" should s"execute test rom '${romFile.getName}'" in {
    val serial = new BufferedSerialEmitter
    val memory = new MappedMemoryController(List(serial, new DumbMemoryBlob))

    val rom = new FileROM(romFile)
    rom.load.zipWithIndex.foreach { case (b,addr) =>
      memory.write(addr.toShort,b)
    }

    val cpu = new CPU(memory)
    while(!serial.output.endsWith("Done")) {
      cpu.tick
    }
    assert(!serial.output.contains("Failed"))
  }
}
