package cpu.instructions.alu

import com.jhood.sgbc.cpu.{A, B, CPU}
import com.jhood.sgbc.cpu.instructions.alu.{SUB}
import com.jhood.sgbc.interrupts.InterruptController
import com.jhood.sgbc.memory.MappedMemoryController
import org.scalatest.WordSpec

class SUBSpec extends WordSpec{
  val memory = MappedMemoryController.empty
  val cpu = new CPU(new InterruptController, memory)

  "An SUB" should {
    "rollover with carry" in {
      val inst = SUB(A,B)
      cpu.write(A,0x01.toByte)
      cpu.write(B,0xFF.toByte)

      inst.execute(cpu)
      assert( cpu.read(A) == 2)
      assert(!cpu.Flags.Z.isSet)
      assert( cpu.Flags.C.isSet)
      assert( cpu.Flags.H.isSet)
      assert( cpu.Flags.N.isSet)
    }

    "do a simple subtract without carry" in {
      val inst = SUB(A,B)
      cpu.write(A,1.toByte)
      cpu.write(B,1.toByte)

      inst.execute(cpu)
      assert( cpu.read(A) == 0)
      assert( cpu.Flags.Z.isSet)
      assert(!cpu.Flags.C.isSet)
      assert(!cpu.Flags.H.isSet)
      assert( cpu.Flags.N.isSet)
    }

    "not sign extend" in {
      val inst = SUB(A,B)
      cpu.write(A,0x80.toByte)
      cpu.write(B,0x01.toByte)

      inst.execute(cpu)
      assert( cpu.read(A) == 0x7F.toByte)
      assert(!cpu.Flags.Z.isSet)
      assert(!cpu.Flags.C.isSet)
      assert( cpu.Flags.H.isSet)
      assert( cpu.Flags.N.isSet)
    }
  }
}
