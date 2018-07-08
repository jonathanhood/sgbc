package com.jhood.sgbc.rom

import java.io.File
import java.nio.file.Files

class FileROM(file: File) {
  private val typeAddr = 0x0147
  private val romSizeAddr = 0x0148
  private val ramSizeAddr = 0x0149

  def load: Array[Byte] = {
    val bytes = Files.readAllBytes(file.toPath)
    if(bytes(romSizeAddr) != 0) {
      throw new Exception("Emulator only supports carts with a single ROM bank for now.")
    } else if(bytes(ramSizeAddr) != 0) {
      throw new Exception("Emulator doesn't support cart RAM for now.")
    } else {
      bytes
    }
  }
}
