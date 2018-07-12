package com.jhood.sgbc.video

import com.jhood.sgbc.memory.MemoryMappedDevice

class Video extends MemoryMappedDevice {
  private val OAM  = (0xFE00,0x0100)
  private val Char = (0x8000,0x1800)
  private val BG1  = (0x9800,0x0400)
  private val BG2  = (0x9C00,0x0400)

  private val ALL = List(OAM,Char,BG1,BG2)

  private val memory: Map[(Int,Int),Array[Byte]] =
    ALL.map(range => range -> Array.fill[Byte](range._2)(0))
       .toMap

  override def providesAddress(addr: Short): Boolean =
    ALL.exists(range => withinRange(range, addr))

  override def write(addr: Short, value: Byte): Unit =
    memory.find(mem => withinRange(mem._1,addr))
          .foreach(mem => mem._2((addr & 0x0FFFF) - mem._1._1) = value)

  override def read(addr: Short): Byte =
    memory.find(mem => withinRange(mem._1,addr))
          .map(mem => mem._2((addr & 0x0FFFF) - mem._1._1))
          .getOrElse(0.toByte)

  private def withinRange(range: (Int,Int), addr: Short): Boolean =
    (addr & 0x0FFFF) >= range._1 && (addr & 0x0FFFF) < (range._1 + range._2)
}
