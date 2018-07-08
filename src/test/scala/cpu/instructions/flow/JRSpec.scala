package cpu.instructions.flow

import com.jhood.sgbc.cpu.CPU
import com.jhood.sgbc.cpu.instructions.flow.JR
import com.jhood.sgbc.memory.MappedMemoryController
import org.scalatest.WordSpec

class JRSpec extends WordSpec {
  val memory = MappedMemoryController.empty

  "A JR" should {
    "unconditionally jump forward" in {
      val cpu = new CPU(memory)
      val inst = JR("JR", _ => true)
      cpu.writePC(0xC001.toShort)
      memory.write(0xC001.toShort, 3)
      inst.execute(cpu)

      // The PC also increments after the jump
      assert(cpu.getAndIncrementPC == (0xC000 + 5).toShort)
    }

    "unconditionally jump backward" in {
      val cpu = new CPU(memory)
      val inst = JR("JR", _ => true)
      cpu.writePC(0xC001.toShort)
      memory.write(0xC001.toShort, - 3)
      inst.execute(cpu)

      // The PC also increments after the jump
      assert(cpu.getAndIncrementPC == (0xC000 - 1).toShort)
    }
  }
}

