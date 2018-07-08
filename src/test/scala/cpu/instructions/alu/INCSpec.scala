package cpu.instructions.alu

import com.jhood.sgbc.cpu._
import com.jhood.sgbc.cpu.instructions.alu.INC
import com.jhood.sgbc.memory.MappedMemoryController
import org.scalatest.WordSpec

class INCSpec extends WordSpec {
  val memory = MappedMemoryController.empty
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

    "not sign extend when setting the highest bit on a register" in {
      val inst = INC(E)
      cpu.write(E,0x7f.toByte)
      cpu.write(D,0xC0.toByte)

      inst.execute(cpu)

      assert(cpu.read(E) == 0x80.toByte)
      assert(cpu.read(D) == 0xC0.toByte)
      assert(cpu.read(DE) == 0xC080.toShort)
    }
  }
}
