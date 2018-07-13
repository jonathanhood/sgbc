package cpu.instructions.alu

import com.jhood.sgbc.cpu.instructions.alu.ADC
import com.jhood.sgbc.cpu.{A, B, CPU, Immediate8}
import com.jhood.sgbc.interrupts.InterruptController
import com.jhood.sgbc.memory.MappedMemoryController
import org.scalatest.WordSpec

class ADCSpec extends WordSpec{
  val memory = MappedMemoryController.empty
  val cpu = new CPU(new InterruptController, memory)

  "An ADC" should {
    "do a basic add" in {
      val inst = ADC(A, B)
      cpu.write(A, 0x01.toByte)
      cpu.write(B, 0x01.toByte)
      cpu.Flags.C.set(true)

      inst.execute(cpu)
      assert(cpu.read(A) == 3)
      assert(!cpu.Flags.Z.isSet)
      assert(!cpu.Flags.C.isSet)
      assert(!cpu.Flags.H.isSet)
      assert(!cpu.Flags.N.isSet)
    }

    val tests = List(
      (0x00, 0x01, false, false),
      (0x01, 0x02, false, false),
      (0x0F, 0x10, false, true),
      (0x10, 0x11, false, false),
      (0x1F, 0x20, false, true),
      (0x7F, 0x80, false, true),
      (0x80, 0x81, false, false),
      (0xF0, 0xF1, false, false),
      (0xFF, 0x00, true, true)
    )

    tests.collect { case (right, expected, carry, halfCarry) =>
      s"do a basic add with imm value ${(right & 0x0FF).toHexString}" in {
        val inst = ADC(A, Immediate8)
        cpu.write(A, 0x00.toByte)
        cpu.writePC(0xC200.toShort)
        cpu.memController.write(0xC200.toShort, right.toByte)
        cpu.Flags.C.set(true)

        inst.execute(cpu)
        assert(cpu.read(A) == expected.toByte)
        assert(cpu.Flags.C.isSet == carry)
        assert(cpu.Flags.H.isSet == halfCarry)
      }
    }

    "do an example from the documentation" in {
      val inst = ADC(A, Immediate8)
      cpu.write(A, 0xE1.toByte)
      cpu.writePC(0xC200.toShort)
      cpu.memController.write(0xC200.toShort, 0x3B.toByte)
      cpu.Flags.C.set(true)

      inst.execute(cpu)
      assert(cpu.read(A) == 0x1D.toByte)
      assert(!cpu.Flags.Z.isSet)
      assert(!cpu.Flags.H.isSet)
      assert(!cpu.Flags.N.isSet)
      assert( cpu.Flags.C.isSet)
    }

    "not sign extend (2-minus)" in {
      val inst = ADC(A,B)
      cpu.write(A,0x7E.toByte)
      cpu.write(B,0x01.toByte)
      cpu.Flags.C.set(true)

      inst.execute(cpu)
      assert( cpu.read(A) == 0x80.toByte)
      assert(!cpu.Flags.Z.isSet)
      assert(!cpu.Flags.C.isSet)
      assert( cpu.Flags.H.isSet)
      assert(!cpu.Flags.N.isSet)
    }

    "not sign extend (1-minus, carry)" in {
      val inst = ADC(A,B)
      cpu.write(A,0x7F.toByte)
      cpu.write(B,0x00.toByte)
      cpu.Flags.C.set(true)

      inst.execute(cpu)
      assert( cpu.read(A) == 0x80.toByte)
      assert(!cpu.Flags.Z.isSet)
      assert(!cpu.Flags.C.isSet)
      assert( cpu.Flags.H.isSet)
      assert(!cpu.Flags.N.isSet)
    }

    "not sign extend (1-minus, no carry)" in {
      val inst = ADC(A,B)
      cpu.write(A,0x7F.toByte)
      cpu.write(B,0x01.toByte)
      cpu.Flags.C.set(false)

      inst.execute(cpu)
      assert( cpu.read(A) == 0x80.toByte)
      assert(!cpu.Flags.Z.isSet)
      assert(!cpu.Flags.C.isSet)
      assert( cpu.Flags.H.isSet)
      assert(!cpu.Flags.N.isSet)
    }

    "roll over to 0 (2-minus)" in {
      val inst = ADC(A,B)
      cpu.write(A,0xFE.toByte)
      cpu.write(B,0x01.toByte)
      cpu.Flags.C.set(true)

      inst.execute(cpu)
      assert( cpu.read(A) == 0x00.toByte)
      assert( cpu.Flags.Z.isSet)
      assert( cpu.Flags.C.isSet)
      assert( cpu.Flags.H.isSet)
      assert(!cpu.Flags.N.isSet)
    }

    "roll over to 0 (1-minus, carry)" in {
      val inst = ADC(A,B)
      cpu.write(A,0xFF.toByte)
      cpu.write(B,0x00.toByte)
      cpu.Flags.C.set(true)

      inst.execute(cpu)
      assert( cpu.read(A) == 0x00.toByte)
      assert( cpu.Flags.Z.isSet)
      assert( cpu.Flags.C.isSet)
      assert( cpu.Flags.H.isSet)
      assert(!cpu.Flags.N.isSet)
    }

    "roll over to 0 (1-minus, no carry)" in {
      val inst = ADC(A,B)
      cpu.write(A,0xFF.toByte)
      cpu.write(B,0x01.toByte)
      cpu.Flags.C.set(false)

      inst.execute(cpu)
      assert( cpu.read(A) == 0x00.toByte)
      assert( cpu.Flags.Z.isSet)
      assert( cpu.Flags.C.isSet)
      assert( cpu.Flags.H.isSet)
      assert(!cpu.Flags.N.isSet)
    }
  }
}
