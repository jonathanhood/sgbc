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
    IME = enabled

  def setEnabled(iType: InterruptType, enabled: Boolean): Unit =
    if(enabled) IE = (IE | iType.mask).toByte
    else IE = (IE & ~iType.mask).toByte

  def enabled(iType: InterruptType): Boolean =
    (IE & iType.mask).toByte == iType.mask

  def masterEnabled: Boolean =
    IME

  private var IME: Boolean = false
  private val IEAddr = 0xFFFF.toShort
  private var IE: Byte = 0
  private val IFAddr = 0xFF0F.toShort
  private var IF: Byte = 0

  override def providesAddress(addr: Short): Boolean =
    addr == IEAddr || addr == IFAddr

  override def write(addr: Short, value: Byte): Unit =
    if(addr == IEAddr) IE = value else IF = value

  override def read(addr: Short): Byte =
    if(addr == IE) IE else IF
}
