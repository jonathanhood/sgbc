package com.jhood.sgbc.input

import com.jhood.sgbc.memory.MemoryMappedDevice

class Input extends MemoryMappedDevice {
  private val P1Addr = 0xFF00.toShort
  private var P1: Byte = 0

  override def providesAddress(addr: Short): Boolean =
    addr == P1Addr

  override def write(addr: Short, value: Byte): Unit =
    P1 = value

  override def read(addr: Short): Byte =
    P1
}
