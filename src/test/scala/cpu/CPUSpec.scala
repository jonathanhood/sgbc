package cpu

import com.jhood.sgbc.cpu.instructions.misc.NOP
import com.jhood.sgbc.cpu.{CPU, PC}
import com.jhood.sgbc.memory.MappedMemoryController
import org.scalatest.WordSpec

class CPUSpec extends WordSpec {
  "A CPU" should {
    "not sign extend when incrementing PC" in {
      val cpu = new CPU(MappedMemoryController.empty)
      cpu.write(PC,0xC17F.toShort)
      cpu.incrementPC(NOP)
      assert(cpu.read(PC) == 0xC180.toShort)
    }
  }
}
