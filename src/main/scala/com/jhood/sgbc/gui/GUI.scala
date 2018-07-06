package com.jhood.sgbc.gui

import java.awt.{Canvas, Color, Graphics, Graphics2D}

import com.jhood.sgbc.memory.MemoryController
import javax.swing.JFrame

sealed abstract class Shade
object Transparent extends Shade
object LightGray extends Shade
object DarkGray extends Shade
object Black extends Shade

class LCD(memory: MemoryController) extends Canvas {
  this.setSize(256,256)

  private def chr(code: Byte): Seq[Shade] =
    (0 until 8*4) flatMap { idx =>
      memory.fetchNibbles((0x8000 + (code << 8) + idx).toShort)
    } collect {
      case 0x0     => Transparent
      case 0x1     => LightGray
      case 0x2     => DarkGray
      case _ : Byte => Black
    }

  private def obj(code: Byte): Seq[Shade] =
    (0 until 16*4) flatMap { idx =>
      memory.fetchNibbles((0x8000 + (code << 9) + idx).toShort)
    } collect {
      case 0x1      => Transparent
      case 0x2      => LightGray
      case 0x3      => DarkGray
      case _ : Byte => Black
    }

  private def bg(base: Short): Seq[Shade] =
    (0 until 0x300) flatMap { idx =>
      chr(memory.fetch((base + idx).toShort))
    }

  private def bg1: Seq[Shade] = bg(0x9800.toShort)
  private def bg2: Seq[Shade] = bg(0x9C00.toShort)
  private def scx: Byte = memory.fetch(0xFF42.toShort)
  private def scy: Byte = memory.fetch(0xFF43.toShort)


  override def paint(graphics: Graphics): Unit = {
    bg1.zipWithIndex.foreach{
      case (shade,idx) =>
        val row = idx / 256
        val col = idx % 256
        shade match {
          case Black =>
            graphics.setColor(Color.BLACK)
            graphics.fillRect(row,col,1,1)
          case LightGray =>
            graphics.setColor(Color.LIGHT_GRAY)
            graphics.fillRect(row,col,1,1)
          case DarkGray =>
            graphics.setColor(Color.DARK_GRAY)
            graphics.fillRect(row,col,1,1)
          case Transparent =>
        }
    }
  }
}

object GUI {
  def main(args: Array[String]): Unit = {
    object EmptyMemoryController extends MemoryController {
      override def fetch(address: Short): Byte = 0
      override def fetchShort(address: Short): Short = 0
      override def fetchNibbles(address: Short): Seq[Byte] = Seq(0x3,0x2,0x1,0)
      override def write(address: Short, value: Byte): Unit = ()
      override def writeShort(address: Short, value: Short): Unit = ()
    }

    val frame = new JFrame()
    frame.setTitle("SGBC")
    frame.setDefaultCloseOperation(3)
    frame.setLocationRelativeTo(null)
    frame.add(new LCD(EmptyMemoryController))
    frame.pack()
    frame.setVisible(true)
  }
}
