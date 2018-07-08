package cpu.instructions.flow

import com.jhood.sgbc.cpu.{CPU, PC}
import com.jhood.sgbc.cpu.instructions.flow.JP
import com.jhood.sgbc.memory.{DumbMemoryBlob, MappedMemoryController}
import org.scalatest.WordSpec

class JPSpec extends WordSpec {
  val memory = new MappedMemoryController(List(new DumbMemoryBlob))
  val cpu = new CPU(memory)

  "A JP" should {
    "unconditionally jump to an address" in {
      val inst = JP("JP", _ => true)
      memory.write(1, 0x12)
      memory.write(2, 0x23)
      inst.execute(cpu)

      assert(cpu.read(PC) == 0x2312)
    }
  }
}
