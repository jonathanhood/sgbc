package com.jhood.sgbc.memory

object MappedMemoryController {
  def basic(rom: MemoryMappedDevice): MappedMemoryController = MappedMemoryController(List(
    rom,                  // Only single banked ROMs for now
    RAM(0xC000,0x2000),   // Game Unit RAM
    RAM(0xFF80,0x7E)      // Zero Page
  ))

  def empty: MappedMemoryController = basic(ROM.empty)
}

case class InvalidReadException(addr: Short) extends Exception(s"Invalid read from address ${addr.toHexString}")
case class InvalidWriteException(addr: Short, value: Byte) extends Exception(s"Invalid write to address ${addr.toHexString} value ${value.toHexString}")

case class MappedMemoryController(devices: List[MemoryMappedDevice])
{
  private val flag = 0xA5.toByte

  final def fetch(address: Short): Byte =
    devices
      .find(_.providesAddress(address))
      .map(_.read(address))
      .getOrElse(throw InvalidReadException(address))

  final def fetchShort(address: Short): Short =
    ((fetch((address + 1).toShort) << 8) + fetch(address)).toShort

  final def fetchNibbles(address: Short): Seq[Byte] = {
    val mem = fetch(address)
    Seq(
      (mem >>> 6) & 0x03,
      (mem >>> 4) & 0x03,
      (mem >>> 2) & 0x03,
      mem & 0x0F
    ).map(_.toByte)
  }

  final def write(address: Short, value: Byte): Unit =
    devices
      .find(_.providesAddress(address))
      .map(_.write(address,value))
      .getOrElse(throw InvalidWriteException(address,value))

  final def writeShort(address: Short, value: Short): Unit = {
    write((address + 1).toShort, (value >>> 8).toByte)
    write(address, value.toByte)
  }

  final def withDevice(device: MemoryMappedDevice): MappedMemoryController =
    MappedMemoryController(devices :+ device)
}
