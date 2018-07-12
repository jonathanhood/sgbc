package com.jhood.sgbc.timer

import com.jhood.sgbc.memory.MemoryMappedDevice

object Timer extends MemoryMappedDevice{
  private val TMAAddr = 0xFF06.toShort // Generate an interrupt when register overflows
  private val TACAddr = 0xFF07.toShort // Time configuration register

  private var TMA: Byte = 0  // 8-bit value
  private var TAC: Byte = 0  // 7-3 (nothing), 2 Timer Start/Stop, 0-1 Clock Select

  override def providesAddress(addr: Short): Boolean =
    addr == TMAAddr || addr == TACAddr

  override def write(addr: Short, value: Byte): Unit =
    if(addr == TMAAddr) TMA = value else TAC = value

  override def read(addr: Short): Byte =
    if(addr == TMAAddr) TMA else TAC
}
