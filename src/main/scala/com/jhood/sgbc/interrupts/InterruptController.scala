package com.jhood.sgbc.interrupts

import com.jhood.sgbc.memory.MemoryMappedDevice

sealed abstract class InterruptType { def mask: Byte; def vector: Short }
object VerticalBlank          extends InterruptType { val mask: Byte = 0x01; val vector: Short = 0x0040 }
object LCDC                   extends InterruptType { val mask: Byte = 0x02; val vector: Short = 0x0048 }
object TimerOverflow          extends InterruptType { val mask: Byte = 0x03; val vector: Short = 0x0050 }
object SerialXFComplete       extends InterruptType { val mask: Byte = 0x04; val vector: Short = 0x0058 }
object PTerminalNegativeEdge  extends InterruptType { val mask: Byte = 0x05; val vector: Short = 0x0060 }

class InterruptController extends MemoryMappedDevice {
  def setMasterEnable(enabled: Boolean): Unit =
    masterInterruptEnable = enabled

  def setEnabled(iType: InterruptType, enabled: Boolean): Unit =
    if(enabled) interruptRegister = interruptRegister | iType.mask
    else interruptRegister = interruptRegister & ~iType.mask

  def enabled(iType: InterruptType): Boolean =
    (interruptRegister & iType.mask) == iType.mask

  def masterEnabled: Boolean =
    masterInterruptEnable

  private var masterInterruptEnable: Boolean = false
  private var interruptRegister = 0

  override def providesAddress(addr: Short): Boolean = addr == 0xFFFF.toShort
  override def write(addr: Short, value: Byte): Unit = ()
  override def read(addr: Short): Byte = 0
}
