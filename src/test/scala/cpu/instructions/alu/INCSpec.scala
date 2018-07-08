package cpu.instructions.alu

import com.jhood.sgbc.cpu.{A, CPU}
import com.jhood.sgbc.cpu.instructions.alu.INC
import com.jhood.sgbc.memory.{DumbMemoryBlob, MappedMemoryController}
import org.scalatest.WordSpec

class INCSpec extends WordSpec {
  val memory = new MappedMemoryController(List(new DumbMemoryBlob))
  val cpu = new CPU(memory)

  "An INC" should {
    "rollover to zero" in {
      val inst = INC(A)
      cpu.write(A,0xFF.toByte)

      inst.execute(cpu)
      assert( cpu.read(A) == 0)
      assert( cpu.Flags.Z.isSet)
      assert( cpu.Flags.C.isSet)
      assert( cpu.Flags.H.isSet)
      assert(!cpu.Flags.N.isSet)
    }
  }
}
