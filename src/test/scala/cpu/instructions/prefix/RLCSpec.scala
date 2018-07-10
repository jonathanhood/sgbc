package cpu.instructions.prefix

import com.jhood.sgbc.cpu.{A, CPU}
import com.jhood.sgbc.cpu.instructions.prefix.{RLC, RR}
import com.jhood.sgbc.interrupts.InterruptController
import com.jhood.sgbc.memory.MappedMemoryController
import org.scalatest.WordSpec

class RLCSpec extends WordSpec {
  val cpu = new CPU(new InterruptController, MappedMemoryController.empty)

  "A RLC" should {
    "rotate into carry and into the least significant bit" in {
      cpu.reset
      val inst = RLC(A)

      cpu.Flags.C.set(false)
      cpu.write(A,0x80.toByte)

      inst.execute(cpu)

      assert(!cpu.Flags.Z.isSet)
      assert(cpu.Flags.C.isSet)
      assert(cpu.read(A) == 0x01.toByte)
    }

    "rotate middle bits" in {
      cpu.reset
      val inst = RLC(A)

      cpu.Flags.C.set(true)
      cpu.write(A,0x10.toByte)

      inst.execute(cpu)

      assert(!cpu.Flags.Z.isSet)
      assert(!cpu.Flags.C.isSet)

      assert(cpu.read(A) == 0x20.toByte)
    }

    "never set the zero flag" in {
      cpu.reset
      val inst = RLC(A)

      cpu.Flags.C.set(true)
      cpu.write(A,0x00.toByte)

      inst.execute(cpu)

      assert(!cpu.Flags.Z.isSet)
      assert(!cpu.Flags.C.isSet)

      assert(cpu.read(A) == 0x00.toByte)
    }

    "should not sign extend which shifting" in {
      cpu.reset
      val inst = RLC(A)

      cpu.Flags.C.set(true)
      cpu.write(A,0xC0.toByte)

      inst.execute(cpu)

      assert(!cpu.Flags.Z.isSet)
      assert(cpu.Flags.C.isSet)

      assert(cpu.read(A) == 0x81.toByte)
    }
  }
}
