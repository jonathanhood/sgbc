package com.jhood.sgbc.memory

class DumbMemoryBlob extends MemoryMappedDevice {
  private val memory : Array[Byte] = Array.fill(0x1FFFF)(0)

  override def providesAddress(addr: Short): Boolean = true
  override def write(addr: Short, value: Byte): Unit = memory(addr & 0x0FFFF) = value
  override def read(addr: Short): Byte = memory(addr & 0x0FFFF)
}
