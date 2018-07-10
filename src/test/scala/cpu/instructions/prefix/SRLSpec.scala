package cpu.instructions.prefix

import com.jhood.sgbc.cpu.{A, CPU}
import com.jhood.sgbc.cpu.instructions.prefix.SRL
import com.jhood.sgbc.interrupts.InterruptController
import com.jhood.sgbc.memory.MappedMemoryController
import org.scalatest.WordSpec

class SRLSpec extends WordSpec {
  val cpu = new CPU(new InterruptController, MappedMemoryController.empty)

  "A SRL" should {
    "shift in zeroes" in {
      cpu.reset
      val inst = SRL(A)
      cpu.write(A, 0.toByte)
      inst.execute(cpu)
      assert(cpu.read(A)  == 0)
      assert( cpu.Flags.Z.isSet)
      assert(!cpu.Flags.C.isSet)
    }

    "shift into carry" in {
      cpu.reset
      val inst = SRL(A)
      cpu.write(A, 1.toByte)
      inst.execute(cpu)
      assert(cpu.read(A)  == 0)
      assert(cpu.Flags.Z.isSet)
      assert(cpu.Flags.C.isSet)
    }

    "shift over top-level bits" in {
      cpu.reset
      val inst = SRL(A)
      cpu.write(A, 0x80.toByte)
      inst.execute(cpu)
      assert(cpu.read(A)  == 0x40)
      assert(!cpu.Flags.Z.isSet)
      assert(!cpu.Flags.C.isSet)
    }
  }
}
