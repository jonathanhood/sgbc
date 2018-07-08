package cpu.instructions.load

import com.jhood.sgbc.cpu._
import com.jhood.sgbc.cpu.instructions.load.LD
import com.jhood.sgbc.memory.MappedMemoryController
import org.scalatest.WordSpec

class LDSpec extends WordSpec {
  val memory = MappedMemoryController.empty
  val cpu = new CPU(memory)

  "A LD instruction" when {
    val registers = List(A,F,B,C,D,E,H,L)
    val registerPairs = registers.flatMap(left => registers.map(right => (left,right)))
    registerPairs.foreach { case (left, right) =>
      s"operating on registers ${left.name}, ${right.name}" should {
        "properly load the target register" in {
          val inst = LD(left, right)
          cpu.write(left, 0xA5.toByte)
          cpu.write(right, 0x5A.toByte)
          inst.execute(cpu)
          assert(cpu.read(left) == 0x5a.toByte)
          assert(cpu.read(right) == 0x5A.toByte)
        }

        "appear correctly when paired into a 16-bit register" in {
          val inst = LD(left, right)
          cpu.write(left, 0xA5.toByte)
          cpu.write(right, 0x5A.toByte)

          val before = cpu.read(left.partOf)
          val after = (before & ~(0xFF << left.offset)) + (0x5A << left.offset)
          inst.execute(cpu)

          assert(cpu.Registers.read(left.partOf) == after.toShort)
        }

        "have a minimal number of cycles for pure register access" in {
          val inst = LD(left, right)
          assert(inst.cycles == 4)
        }
      }
    }

    val wideRegisters = List(AF,BC,DE,HL)
    val memoryReads = registers
      .flatMap(left => wideRegisters.map(right => (left,Memory8(right))))
      .filterNot(pair => pair._1.partOf == pair._2.addrSource)

    memoryReads.foreach { case (reg,mem) =>
      s"loading ${reg.name} from memory ${mem.name}" should {
        "read from memory" in {
          val inst = LD(reg, mem)
          val addr = 0xC000.toShort

          cpu.write(mem.addrSource, addr)
          memory.write(addr, 0xAF.toByte)

          inst.execute(cpu)

          assert(cpu.read(reg) == 0xAF.toByte)
          assert(cpu.read(mem) == 0xAF.toByte)
          assert(cpu.read(mem.addrSource) == addr)
        }

        "have extra cycles for the memory read" in {
          val inst = LD(reg, mem)
          assert(inst.cycles == 8)
        }
      }

      s"loading ${reg.name} into memory ${mem.name}" should {
        "read from memory" in {
          val inst = LD(mem, reg)
          val addr = 0xC000.toShort

          cpu.write(mem.addrSource, addr)
          memory.write(addr, 0xEB.toByte)
          cpu.write(reg, 0xBE.toByte)

          inst.execute(cpu)

          assert(cpu.read(reg) == 0xBE.toByte)
          assert(cpu.read(mem) == 0xBE.toByte)
          assert(cpu.read(mem.addrSource) == addr)
        }

        "have extra cycles for the memory read" in {
          val inst = LD(mem, reg)
          assert(inst.cycles == 8)
        }
      }
    }

    registers.foreach { register =>
      s"loading immediate values in ${register.name}" should {
        "load the immediate" in {
          val inst = LD(register, Immediate8)
          cpu.writePC(0xC000.toShort)
          memory.write(0xC000.toShort, 0xEA.toByte)
          cpu.write(register, 0xAE.toByte)

          inst.execute(cpu)

          assert(cpu.read(register) == 0xEA.toByte)
        }

        "have extra cycles for the memory read" in {
          val inst = LD(register, Immediate8)
          assert(inst.cycles == 8)
        }
      }
    }

    registers.filterNot(_.partOf == HL).foreach { reg =>
      s"incrementing HL and operating on register ${reg.name}" should {
        s"load register ${reg.name}" in {
          val inst = LD(reg,Memory8(HLI))
          cpu.write(HL, 0xC000.toShort)
          memory.write(0xC000.toShort, 5)

          inst.execute(cpu)

          assert(cpu.read(HL) == (0xC000 + 1).toShort)
          assert(cpu.read(reg) == 5)
        }

        "have no extra cycles for the increment" in {
          val inst = LD(reg,Memory8(HLI))
          assert(inst.cycles == 8)
        }
      }

      s"decrementing HL and operating on register ${reg.name}" should {
        s"load register ${reg.name}" in {
          val inst = LD(reg,Memory8(HLD))
          cpu.write(HL, 0xC000.toShort)
          memory.write(0xC000.toShort, 5)

          inst.execute(cpu)

          assert(cpu.read(HL) == (0xC000 - 1).toShort)
          assert(cpu.read(reg) == 5)
        }

        "have no extra cycles for the increment" in {
          val inst = LD(reg,Memory8(HLI))
          assert(inst.cycles == 8)
        }
      }
    }
  }
}
