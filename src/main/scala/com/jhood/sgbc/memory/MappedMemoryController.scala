package com.jhood.sgbc.memory

class MappedMemoryController(devices: List[MemoryMappedDevice])
  extends MemoryController
{
  private val flag = 0xA5.toByte

  override def fetch(address: Short): Byte =
    devices
      .find(_.providesAddress(address))
      .map(_.read(address))
      .getOrElse(flag)

  override def fetchShort(address: Short): Short =
    ((fetch((address + 1).toShort) << 8) + fetch(address)).toShort

  override def fetchNibbles(address: Short): Seq[Byte] = {
    val mem = fetch(address)
    Seq(
      (mem >>> 6) & 0x03,
      (mem >>> 4) & 0x03,
      (mem >>> 2) & 0x03,
      mem & 0x0F
    ).map(_.toByte)
  }

  override def write(address: Short, value: Byte): Unit =
    devices.find(_.providesAddress(address)).foreach(_.write(address,value))

  override def writeShort(address: Short, value: Short): Unit = {
    write((address + 1).toShort, (value >>> 8).toByte)
    write(address, value.toByte)
  }
}
