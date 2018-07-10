package cpu.instructions.prefix

import com.jhood.sgbc.cpu.instructions.prefix.RR
import com.jhood.sgbc.cpu.{A, CPU}
import com.jhood.sgbc.interrupts.InterruptController
import com.jhood.sgbc.memory.MappedMemoryController
import org.scalatest.WordSpec

class RRSpec extends WordSpec {
  val cpu = new CPU(new InterruptController, MappedMemoryController.empty)

  "A RR" should {
    "rotate into carry" in {
      cpu.reset
      val inst = RR(A)

      cpu.Flags.C.set(false)
      cpu.write(A,0x01.toByte)

      inst.execute(cpu)

      assert(cpu.Flags.Z.isSet)
      assert(cpu.Flags.C.isSet)
      assert(cpu.read(A) == 0x00.toByte)
    }

    "rotate out of carry" in {
      cpu.reset
      val inst = RR(A)

      cpu.Flags.C.set(true)
      cpu.write(A,0x00.toByte)

      inst.execute(cpu)

      assert(!cpu.Flags.Z.isSet)
      assert(!cpu.Flags.C.isSet)

      assert(cpu.read(A) == 0x80.toByte)
    }
  }
}
