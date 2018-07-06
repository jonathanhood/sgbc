package com.jhood.sgbc.memory

trait MemoryController {
  def fetch(address: Short): Byte
  def fetchShort(address: Short): Short
  def write(address: Short, value: Byte): Unit
}
