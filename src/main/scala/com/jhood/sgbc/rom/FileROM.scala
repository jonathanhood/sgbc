package com.jhood.sgbc.rom

import java.io.File
import java.nio.file.Files

class FileROM(file: File) {
  def load: Array[Byte] = Files.readAllBytes(file.toPath)
}
