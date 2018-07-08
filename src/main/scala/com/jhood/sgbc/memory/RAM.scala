package com.jhood.sgbc.memory

case class RAM(baseAddress: Int, size: Int) extends MemoryMappedDevice {
  private val memory : Array[Byte] = Array.fill(size)(0)

  override def providesAddress(addr: Short): Boolean =
    (addr & 0x0FFFF) >= baseAddress && (addr & 0x0FFFF) < (baseAddress + size)

  override def write(addr: Short, value: Byte): Unit =
    memory((addr & 0x0FFFF) - baseAddress) = value

  override def read(addr: Short): Byte =
    memory((addr & 0x0FFFF) - baseAddress)
}
