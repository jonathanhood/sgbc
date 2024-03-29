package cpu.instructions.alu

import com.jhood.sgbc.cpu._
import com.jhood.sgbc.cpu.instructions.alu.ADD16
import com.jhood.sgbc.interrupts.InterruptController
import com.jhood.sgbc.memory.MappedMemoryController
import org.scalatest.WordSpec

class ADD16Spec extends WordSpec{
  val memory = MappedMemoryController.empty
  val cpu = new CPU(new InterruptController, memory)

  "An ADD16" should {
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

    val plusTests = List(
      // (operand, result, half-carry, carry)
      (0x0000,0x0001,false,false),
      (0x0001,0x0002,false,false),
      (0x000F,0x0010,false,false),
      (0x0010,0x0011,false,false),
      (0x001F,0x0020,false,false),
      (0x007F,0x0080,false,false),
      (0x0080,0x0081,false,false),
      (0x0100,0x0101,false,false),
      (0x0F00,0x0F01,false,false),
      (0x1F00,0x1F01,false,false),
      (0x1000,0x1001,false,false),
      (0x7FFF,0x8000,true,false),
      (0x8000,0x8001,false,false),
      (0xFFFF,0x0000,true,true)
    )

    for(test <- plusTests) {
      val (value,expected,halfCarry,carry) = test

      s"execute add an immediate 1 to ${value}" in {
        val inst = ADD16(BC,PaddedImmediate8,true)
        cpu.writePC(0xC200.toShort)
        cpu.memController.write(0xC200.toShort, 1.toByte)
        cpu.write(BC,value.toShort)

        inst.execute(cpu)

        assertResult(expected.toShort)(cpu.read(BC))
        assertResult(carry)(cpu.Flags.C.isSet)
        assertResult(halfCarry)(cpu.Flags.H.isSet)
        assertResult(false)(cpu.Flags.N.isSet)
        assertResult(false)(cpu.Flags.Z.isSet)
        assertResult(0xC201.toShort)(cpu.getAndIncrementPC)
      }
    }

    val minusTests = List(
      // (operand, expected, half-carry, carry)
      (0x0000,0xFFFF,false,false),
      (0x0001,0x0000,true,true),
      (0x000F,0x000E,true,true),
      (0x0010,0x000F,true,true),
      (0x001F,0x001E,true,true),
      (0x007F,0x007E,true,true),
      (0x0080,0x007F,true,true),
      (0x0100,0x00FF,true,true),
      (0x0F00,0x0EFF,true,true),
      (0x1F00,0x1EFF,true,true),
      (0x1000,0x0FFF,false,true),
      (0x7FFF,0x7FFE,true,true),
      (0x8000,0x7FFF,false,true),
      (0xFFFF,0xFFFE,true,true)
    )

    for(test <- minusTests) {
      val (value,expected,halfCarry,carry) = test

      s"execute add an immediate -1 to 0x${value.toHexString}" in {
        val inst = ADD16(BC,PaddedImmediate8,true)
        cpu.writePC(0xC200.toShort)
        cpu.memController.write(0xC200.toShort, (-1).toByte)
        cpu.write(BC,value.toShort)

        inst.execute(cpu)

        assertResult(expected.toShort)(cpu.read(BC))
        assertResult(carry)(cpu.Flags.C.isSet)
        assertResult(halfCarry)(cpu.Flags.H.isSet)
        assertResult(false)(cpu.Flags.N.isSet)
        assertResult(false)(cpu.Flags.Z.isSet)
        assertResult(0xC201.toShort)(cpu.getAndIncrementPC)
      }
    }
  }
}
