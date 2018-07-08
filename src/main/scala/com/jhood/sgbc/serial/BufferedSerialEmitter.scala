package com.jhood.sgbc.serial

import com.jhood.sgbc.memory.MemoryMappedDevice

class BufferedSerialEmitter extends MemoryMappedDevice {
  val SBAddr = 0xFF01.toShort
  val SCAddr = 0xFF02.toShort

  var output: String = ""
  var SB: Byte = 0

  override def providesAddress(addr: Short): Boolean =
    addr == SBAddr || addr == SCAddr

  override def write(addr: Short, value: Byte): Unit =
    if(addr == SBAddr) {
      SB = value
    } else {
      output = output + SB.toChar
    }

  override def read(addr: Short): Byte =
    if(addr == SBAddr) SB else 0
}
