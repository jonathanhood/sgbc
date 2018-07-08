package cpu

import com.jhood.sgbc.cpu.CPU
import com.jhood.sgbc.memory.MappedMemoryController
import org.scalatest.WordSpec

class CPUSpec extends WordSpec {
  "A CPU" should {
    "not sign extend when incrementing PC" in {
      val cpu = new CPU(MappedMemoryController.empty)
      cpu.writePC(0xC17F.toShort)
      cpu.getAndIncrementPC
      assert(cpu.getAndIncrementPC == 0xC180.toShort)
    }
  }
}
