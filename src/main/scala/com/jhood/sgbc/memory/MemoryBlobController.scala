package com.jhood.sgbc.memory

class MemoryBlobController extends MemoryController {
  private val memory = new Array[Byte](0xFFFF)

  override def fetch(address: Short): Byte = memory(address)

  override def fetchShort(address: Short): Short = ((memory(address + 1) << 8) + memory(address)).toShort

  override def fetchNibbles(address: Short): Seq[Byte] = {
    val mem = memory(address)
    Seq(
      (mem >>> 6) & 0x03,
      (mem >>> 4) & 0x03,
      (mem >>> 2) & 0x03,
      mem & 0x0F
    ).map(_.toByte)
  }

  override def write(address: Short, value: Byte): Unit =
    memory(address) = value

  override def writeShort(address: Short, value: Short): Unit = {
    memory(address + 1) = (value >>> 8).toByte
    memory(address) = value.toByte
  }
}
