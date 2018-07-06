package com.jhood.sgbc.lr35902

class LR35902 {
  def tick(state: CpuState, memory: Memory): CpuState =
    memory.fetch(state.pc) match {
      // nop
      case 0x00 =>
        state.pc_inc(1)

      // LD BC,d16
      case 0x01 =>
        val addr = memory.fetchShort((state.pc + 1).toShort)
        val data = memory.fetchShort(addr)
        state.bc_set(data).pc_inc(3)

      // LC (BC),A
      case 0x02 =>
        val data = memory.fetch(state.bc)
        state.a_set(data).pc_inc(1)

      // INC BC
      case 0x03 =>
        state.bc_inc

      case 0x04 =>

    }
}
