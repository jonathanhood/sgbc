package com.jhood.sgbc.serial

import com.jhood.sgbc.memory.MemoryMappedDevice

class BufferedSerialEmitter extends MemoryMappedDevice {
  val SBAddr = 0xFF01
  val SCAddr = 0xFF02

  var SB = 0
  var output: String = ""

  override def providesAddress(addr: Short): Boolean =
    addr == SBAddr || addr == SCAddr

  override def write(addr: Short, value: Byte): Unit =
    if(SB == SBAddr) {
      SB = value
    } else if((value & 0x10) != 0) {
      print(value.toChar)
      output += value.toChar
    }

  override def read(addr: Short): Byte =
    if(addr == SBAddr) SB.toByte else 0
}
