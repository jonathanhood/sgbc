package com.jhood.sgbc.video

import com.jhood.sgbc.memory.MemoryMappedDevice

class Video extends MemoryMappedDevice {
  private val LCDC = 0

  private val OAM  = (0xFE00,0x0100)
  private val Char = (0x8000,0x1800)
  private val BG1  = (0x9800,0x0400)
  private val BG2  = (0x9C00,0x0400)
  private val Regs = (0xFF40,0x0010)

  private val ALL = List(OAM,Char,BG1,BG2,Regs)

  private val memory: Map[(Int,Int),Array[Byte]] =
    ALL.map(range => range -> Array.fill[Byte](range._2)(0))
       .toMap

  override def providesAddress(addr: Short): Boolean =
    ALL.exists(range => withinRange(range, addr))

  override def write(addr: Short, value: Byte): Unit =
    memory.find(mem => withinRange(mem._1,addr))
           .foreach(mem =>
             mem._2((addr & 0x0FFFF) - mem._1._1) = value
           )

  override def read(addr: Short): Byte =
    memory.find(mem => withinRange(mem._1,addr))
           .map(mem => mem._2((addr & 0x0FFFF) - mem._1._1))
           .getOrElse(0.toByte)

  def draw: Iterator[Int] =
   (0 until 32).iterator
               .flatMap(x => (0 until 32).map(y => (x,y)))
               .map(codeForPixel)
               .flatMap(dotsForPixel)

  private def codeForPixel(xy: (Int,Int)): (Int,Int,Int) = {
    val index = (xy._2 * 32) + (xy._1 % 32)
    (xy._1, xy._2, memory(selectedBG)(index.toShort) & 0x0FF)
  }

  private def dotsForPixel(xycode: (Int,Int,Int)): Iterator[Int] =
    (xycode._3 until xycode._3 + 0xF).iterator
                                     .map(charData)
                                     .flatMap(byteToNibbles)

  private def byteToNibbles(byte: Byte): List[Int] =
    List(byte & 0x03, (byte >>> 2) & 0x03, (byte >>> 4) & 0x03, (byte >>> 6) & 0x03)

  private def charData(index: Int): Byte =
    if((memory(Regs)(LCDC) & 0x10) == 0x10) memory(Char)(index)
    else memory(Char)(index + 0x800)

  private def withinRange(range: (Int,Int), addr: Short): Boolean =
    (addr & 0x0FFFF) >= range._1 && (addr & 0x0FFFF) < (range._1 + range._2)

  private def selectedBG: (Int,Int) =
    if((memory(Regs)(LCDC) & 0x08) == 0x08) BG2
    else BG1
}
