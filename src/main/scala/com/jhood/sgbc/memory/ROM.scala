package com.jhood.sgbc.memory

import java.io.File
import java.nio.file.Files

object ROM {
  def fromFile(file: File): ROM = ROM(Files.readAllBytes(file.toPath))
  lazy val empty: ROM = ROM(Array.fill(32*1024)(0))
}

case class ROM(data: Array[Byte]) extends MemoryMappedDevice {
  override def providesAddress(addr: Short): Boolean =
    (addr & 0x0FFFF) < 32*1024

  override def write(addr: Short, value: Byte): Unit =
    throw new Exception("Cannot write to ROM")

  override def read(addr: Short): Byte =
    data(addr & 0x0FFFF)
}
