package integration

import java.io.File

import com.jhood.sgbc.cpu.CPU
import com.jhood.sgbc.memory.{MappedMemoryController, RAM, ROM}
import com.jhood.sgbc.serial.BufferedSerialEmitter
import org.scalatest.FlatSpec

class LDTests extends ROMTestExecutor(new File("./src/test/roms/06-ld r,r.gb"))

abstract class ROMTestExecutor(romFile: File) extends FlatSpec  {
  "A GameBoy" should s"execute test rom '${romFile.getName}'" in {
    val serial = new BufferedSerialEmitter
    val memory = MappedMemoryController.
      basic(ROM.fromFile(romFile)).
      withDevice(serial).
      withDevice(RAM(0xFF40, 0x10))

    val cpu = new CPU(memory)
    try {
      while (!cpu.Status.stopped) {
        cpu.tick
      }
    } catch {
      case ex: Exception => throw ex
    }
    assert(!serial.output.contains("Failed"))
  }
}
