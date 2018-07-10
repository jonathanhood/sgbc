package cpu.instructions.flow

import com.jhood.sgbc.cpu.{CPU, Immediate16}
import com.jhood.sgbc.cpu.instructions.flow.JP
import com.jhood.sgbc.interrupts.InterruptController
import com.jhood.sgbc.memory.MappedMemoryController
import org.scalatest.WordSpec

class JPSpec extends WordSpec {
  val memory = MappedMemoryController.empty
  val cpu = new CPU(new InterruptController, memory)

  "A JP" should {
    "unconditionally jump to an address" in {
      val inst = JP(Immediate16, "JP", _ => true)
      cpu.writePC(0xC001.toShort)
      memory.write(0xC001.toShort, 0x12)
      memory.write(0xC002.toShort, 0x23)
      inst.execute(cpu)

      assert(cpu.getAndIncrementPC == 0x2312)
    }
  }
}
