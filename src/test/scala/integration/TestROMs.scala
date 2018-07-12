package integration

import java.io.File

import com.jhood.sgbc.cpu.CPU
import com.jhood.sgbc.input.Input
import com.jhood.sgbc.interrupts.InterruptController
import com.jhood.sgbc.memory.{MappedMemoryController, RAM, ROM}
import com.jhood.sgbc.serial.BufferedSerialEmitter
import com.jhood.sgbc.sound.Sound
import com.jhood.sgbc.timer.Timer
import com.jhood.sgbc.video.Video
import org.scalatest.FlatSpec

class InstructionTests extends ROMTestExecutor(new File("./src/test/roms/06-ld r,r.gb"))

abstract class ROMTestExecutor(romFile: File) extends FlatSpec  {
  "A GameBoy" should s"execute test rom '${romFile.getName}'" in {
    val serial = new BufferedSerialEmitter
    val interrupts = new InterruptController
    val video = new Video
    val memory = MappedMemoryController.basic(ROM.fromFile(romFile))
      .withDevice(serial)
      .withDevice(interrupts)
      .withDevice(Timer)
      .withDevice(new Sound)
      .withDevice(new Input)
      .withDevice(video)

    val cpu = new CPU(interrupts,memory)
    try {
      while (!serial.output.contains("Passed")) {
        cpu.tick
      }
    } catch {
      case ex: Exception => throw ex
    }
    assert(serial.output.contains("Passed"))
  }
}
