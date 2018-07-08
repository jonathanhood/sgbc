package cpu.instructions.flow

import com.jhood.sgbc.cpu.{CPU, PC}
import com.jhood.sgbc.cpu.instructions.flow.JR
import com.jhood.sgbc.memory.{DumbMemoryBlob, MappedMemoryController}
import org.scalatest.WordSpec

class JRSpec extends WordSpec {
  val memory = new MappedMemoryController(List(new DumbMemoryBlob))

  "A JR" should {
    "unconditionally jump forward" in {
      val cpu = new CPU(memory)
      val inst = JR("JR", _ => true)
      memory.write(1,3)
      inst.execute(cpu)

      assert(cpu.read(PC) == 3)
    }

    "unconditionally jump backward" in {
      val cpu = new CPU(memory)
      val inst = JR("JR", _ => true)
      memory.write(1, - 3)
      inst.execute(cpu)

      assert(cpu.read(PC) == (0xFFFF - 2).toShort)
    }
  }
}

