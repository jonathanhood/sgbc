package com.jhood.sgbc.lr35902

case class CpuState(af : Short, bc: Short, de: Short, hl: Short, sp: Short, pc: Short) {
  lazy val a: Byte = upper(af)
  lazy val f: Byte = lower(af)
  lazy val b: Byte = upper(bc)
  lazy val c: Byte = lower(bc)
  lazy val d: Byte = upper(de)
  lazy val e: Byte = lower(de)
  lazy val h: Byte = upper(hl)
  lazy val l: Byte = lower(hl)

  def pc_inc(increment: Int): CpuState =
    copy(pc = (pc + increment).toShort)

  def a_set(data: Byte): CpuState =
    copy(af = ((data << 8) + f).toShort)

  def b_inc: CpuState =
    copy(bc = combined((b + 1).toByte, c))

  def bc_set(data: Short): CpuState =
    copy(bc = data)

  def bc_inc: CpuState =
    copy(bc = (bc + 1).toShort)

  private def upper(reg: Short): Byte = ((reg >>> 8) & 0x00FF).toByte
  private def lower(reg: Short): Byte = (reg & 0x00FF).toByte
  private def combined(upper: Byte, lower: Byte): Short = ((upper << 8) + lower).toShort
}
