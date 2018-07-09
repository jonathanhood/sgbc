package com.jhood.sgbc.memory

case class MBC1(romBanks: List[Array[Byte]], ramBanks: List[Array[Byte]]) extends MemoryMappedDevice {
  private val RAM           = (0xA000,0x2000)
  private val ROM           = (0x4000,0x4000)
  private val ROM0          = (0x0000,0x4000)

  private val ROMSelect     = (0x2000,0x1000)

  private var selectedROM = 1
  private var selectedRAM = 0

  override def providesAddress(addr: Short): Boolean =
    (within(addr,RAM) && ramBanks.size > selectedRAM) ||
      within(addr,ROM0) ||
      within(addr,ROM)  ||
      within(addr,ROMSelect)

  override def write(addr: Short, value: Byte): Unit =
    if(within(addr,ROMSelect)) {
      selectedROM = value
    } else {
      throw new Exception(s"Invalid write of address 0x${(addr & 0x0FFFF).toHexString}")
    }

  override def read(addr: Short): Byte =
    if(within(addr,ROM0)) {
      romBanks.head(addr & 0x0FFFF)
    } else if(within(addr,RAM) && ramBanks.size > selectedRAM) {
      val ramAddr = (addr - RAM._1) & 0x0FFFF
      ramBanks(selectedRAM)(ramAddr)
    } else if(within(addr,ROM)) {
      val romAddr = (addr - ROM._1) & 0x0FFFF
      romBanks(selectedROM)(romAddr)
    } else {
      throw new Exception(s"Invalid read of address 0x${(addr & 0x0FFFF).toHexString}")
    }

  private def within(addr: Short, range: (Int,Int)): Boolean =
    (addr & 0x0FFFF) >= range._1 && (addr & 0x0FFFF) < (range._1 + range._2)
}
