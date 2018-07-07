package com.jhood.sgbc.memory

trait MemoryMappedDevice {
  def providesAddress(addr: Short): Boolean
  def write(addr: Short, value: Byte): Unit
  def read(addr: Short): Byte
}
