package com.jhood.sgbc.memory

object MappedMemoryController {
  def basic(rom: ROM): MappedMemoryController = MappedMemoryController(List(
    rom,                  // Only single banked ROMs for now
    RAM(0x8000,0x1800),   // Character Data
    RAM(0x9800,0x0400),   // Background 1
    RAM(0x9C00,0x0400),   // Background 2
    // Game Pack RAM (0xA000 - 0xBFFF)
    RAM(0xC000,0x2000),   // Game Unit RAM
    // Echo RAM (0xE000 - 0xFDFF)
    RAM(0xFE00,0x0100),   // OAM
    // Registers (0xFF00 - 0xFF7F)
    RAM(0xFF80,0x7E)      // Zero Page
  ))

  def empty: MappedMemoryController = basic(ROM.empty)
}

case class MappedMemoryController(devices: List[MemoryMappedDevice])
{
  private val flag = 0xA5.toByte

  final def fetch(address: Short): Byte =
    devices
      .find(_.providesAddress(address))
      .map(_.read(address))
      .getOrElse(throw new Exception(s"Invalid access to address ${address.toHexString}"))

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
    devices.find(_.providesAddress(address)).foreach(_.write(address,value))

  final def writeShort(address: Short, value: Short): Unit = {
    write((address + 1).toShort, (value >>> 8).toByte)
    write(address, value.toByte)
  }

  final def withDevice(device: MemoryMappedDevice): MappedMemoryController =
    MappedMemoryController(devices :+ device)
}
