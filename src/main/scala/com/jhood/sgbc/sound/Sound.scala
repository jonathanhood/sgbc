package com.jhood.sgbc.sound

import com.jhood.sgbc.memory.MemoryMappedDevice

class Sound extends MemoryMappedDevice{
  private val NR50Addr = 0xFF24.toShort
  private val NR51Addr = 0xFF25.toShort
  private val NR52Addr = 0xFF26.toShort

  private var NR50: Byte = 0
  private var NR51: Byte = 0
  private var NR52: Byte = 0

  override def providesAddress(addr: Short): Boolean =
    addr == NR50Addr || addr == NR51Addr || addr == NR52Addr

  override def write(addr: Short, value: Byte): Unit =
    if(addr == NR50Addr) NR50 = value
    else if(addr == NR51Addr) NR51 = value
    else NR52 = value

  override def read(addr: Short): Byte =
    if(addr == NR50Addr) NR50
    else if(addr == NR51Addr) NR51
    else NR52
}
