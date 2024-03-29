package integration

import java.io.File

import com.jhood.sgbc.cpu.CPU
import com.jhood.sgbc.cpu.instructions.misc.NOP
import com.jhood.sgbc.input.Input
import com.jhood.sgbc.interrupts.InterruptController
import com.jhood.sgbc.memory.{MappedMemoryController, RAM, ROM}
import com.jhood.sgbc.serial.BufferedSerialEmitter
import com.jhood.sgbc.sound.Sound
import com.jhood.sgbc.timer.Timer
import com.jhood.sgbc.video.Video
import org.scalatest.FlatSpec

class InstructionTests3 extends ROMTestExecutor(new File("./src/test/roms/03-op sp,hl.gb"))
class InstructionTests4 extends ROMTestExecutor(new File("./src/test/roms/04-op r,imm.gb"))
class InstructionTests5 extends ROMTestExecutor(new File("./src/test/roms/05-op rp.gb"))
class InstructionTests6 extends ROMTestExecutor(new File("./src/test/roms/06-ld r,r.gb"))
class InstructionTests7 extends ROMTestExecutor(new File("./src/test/roms/07-jr,jp,call,ret,rst.gb"))
class InstructionTests8 extends ROMTestExecutor(new File("./src/test/roms/08-misc instrs.gb"))
class InstructionTests9 extends ROMTestExecutor(new File("./src/test/roms/09-op r,r.gb"))

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
      var nopCounter = 10
      val before = System.currentTimeMillis()
      while (!serial.output.contains("Passed") && !serial.output.contains("Failed") && nopCounter > 0) {
        if(cpu.tick == NOP) nopCounter -= 1 else nopCounter = 10
      }
      val after = System.currentTimeMillis()
      println("")
      println(s"ROM test execution took ${after-before}ms")
    } catch {
      case ex: Exception => throw ex
    }
    assert(serial.output.contains("Passed"))
  }
}
