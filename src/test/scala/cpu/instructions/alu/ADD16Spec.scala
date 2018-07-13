package cpu.instructions.alu

import com.jhood.sgbc.cpu._
import com.jhood.sgbc.cpu.instructions.alu.ADD16
import com.jhood.sgbc.interrupts.InterruptController
import com.jhood.sgbc.memory.MappedMemoryController
import org.scalatest.WordSpec

class ADD16Spec extends WordSpec{
  val memory = MappedMemoryController.empty
  val cpu = new CPU(new InterruptController, memory)

  "An INC" should {
    "rollover to zero" in {
      val inst = ADD16(BC,DE,false)
      cpu.write(BC,0xFFFF.toShort)
      cpu.write(DE,0x01.toShort)

      inst.execute(cpu)
      assert( cpu.read(BC) == 0)
      assert( cpu.Flags.C.isSet)
      assert( cpu.Flags.H.isSet)
      assert(!cpu.Flags.N.isSet)
    }

    "do a simple add without carry" in {
      val inst = ADD16(BC,DE,false)
      cpu.write(BC,1.toShort)
      cpu.write(DE,1.toShort)

      inst.execute(cpu)
      assert( cpu.read(BC) == 2)
      assert(!cpu.Flags.C.isSet)
      assert(!cpu.Flags.H.isSet)
      assert(!cpu.Flags.N.isSet)
    }

    "not sign extend" in {
      val inst = ADD16(BC,DE,false)
      cpu.write(BC,0x7FFF.toShort)
      cpu.write(DE,0x01.toShort)

      inst.execute(cpu)
      assert( cpu.read(BC) == 0x8000.toShort)
      assert(!cpu.Flags.C.isSet)
      assert( cpu.Flags.H.isSet)
      assert(!cpu.Flags.N.isSet)
    }

    "sign extend (negative) a 8-bit right operand" in {
      val inst = ADD16(BC,PaddedImmediate8,false)
      cpu.writePC(0xC200.toShort)
      cpu.memController.write(0xC200.toShort, 0xFF.toByte)

      cpu.write(BC,0.toShort)

      inst.execute(cpu)
      assert( cpu.read(BC) == 0xFFFF.toShort)
      assert(!cpu.Flags.C.isSet)
      assert(!cpu.Flags.H.isSet)
      assert(!cpu.Flags.N.isSet)
    }

    "sign extend (positive) a 8-bit right operand" in {
      val inst = ADD16(BC,PaddedImmediate8,false)
      cpu.writePC(0xC200.toShort)
      cpu.memController.write(0xC200.toShort, 1.toByte)

      cpu.write(BC,0.toShort)

      inst.execute(cpu)
      assert( cpu.read(BC) == 1.toShort)
      assert(!cpu.Flags.C.isSet)
      assert(!cpu.Flags.H.isSet)
      assert(!cpu.Flags.N.isSet)
    }
  }
}
