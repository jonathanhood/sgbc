package com.jhood.sgbc.memory

import java.io.File
import java.nio.file.Files

object ROM {
  private def romBankDescription(sizeCode: Int): (Int,Int) = sizeCode match {
    case 0 => (  32*1024, 2)
    case 1 => (  64*1024, 4)
    case 2 => ( 128*1024, 8)
    case 3 => (1024*1024,16)
    case _ => throw new Exception("Unsupported ROM")
  }

  private def ramBankDescription(sizeCode: Int): Option[(Int,Int)] = sizeCode match {
    case 0 => None
    case 1 => Some(  2*1024, 1)
    case 2 => Some(  8*1024, 1)
    case 3 => Some( 32*1024, 4)
    case 6 => Some(128*1024,16)
    case _ => throw new Exception("Unsupported ROM")
  }


  def fromFile(file: File): MemoryMappedDevice = {
    val bytes = Files.readAllBytes(file.toPath)
    val cartType = bytes(0x0147)
    val romSize  = bytes(0x0148)
    val ramSize  = bytes(0x0148)
    if(cartType == 0) {
      ROM(bytes)
    } else if(cartType == 1) {
      val ramDesc = ramBankDescription(ramSize)
      val romDesc = romBankDescription(romSize)
      val romBanks = (0 until romDesc._2).map { bank =>
        val bankSize = romDesc._1 / romDesc._2
        val startIndex = bank * bankSize
        bytes.slice(startIndex, startIndex + bankSize)
      }.toList

      val ramBanks = ramDesc.map { desc =>
        (0 until desc._2).map { bank =>
          Array.fill[Byte](desc._1)(0)
        }.toList
      }.getOrElse(Nil)
      MBC1(romBanks,ramBanks)
    } else {
      throw new Exception("Unsupported ROM")
    }
  }

  lazy val empty: ROM = ROM(Array.fill(32*1024)(0))
}

case class ROM(data: Array[Byte]) extends MemoryMappedDevice {
  override def providesAddress(addr: Short): Boolean =
    (addr & 0x0FFFF) < 32*1024

  override def write(addr: Short, value: Byte): Unit =
    throw new Exception(s"Cannot write to ROM address 0x${(addr & 0x0FFFF).toHexString}")

  override def read(addr: Short): Byte =
    data(addr & 0x0FFFF)
}
