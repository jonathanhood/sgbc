package integration

import java.io.File

import com.jhood.sgbc.cpu.CPU
import com.jhood.sgbc.interrupts.InterruptController
import com.jhood.sgbc.memory.{MappedMemoryController, RAM, ROM}
import com.jhood.sgbc.serial.BufferedSerialEmitter
import org.scalatest.FlatSpec

class InstructionTests extends ROMTestExecutor(new File("./src/test/roms/cpu_instrs.gb"))

abstract class ROMTestExecutor(romFile: File) extends FlatSpec  {
  "A GameBoy" should s"execute test rom '${romFile.getName}'" in {
    val serial = new BufferedSerialEmitter
    val interrupts = new InterruptController
    val memory = MappedMemoryController.
      basic(ROM.fromFile(romFile)).
      withDevice(serial).
      withDevice(RAM(0xFF40, 0x10)).
      withDevice(interrupts)

    val cpu = new CPU(interrupts,memory)
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
