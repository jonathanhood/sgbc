package com.jhood.sgbc.gui

import java.awt.{Canvas, Color, Graphics, Graphics2D}

import javax.swing.JFrame

class LCD extends Canvas {
  this.setSize(144,160)

  override def paint(graphics: Graphics): Unit = {
    val pixels = List(
      (0,0),
      (0,1),
      (0,2),
      (0,3),
      (0,4),
      (1,4),
      (2,4),
      (3,4),
      (4,4),
      (4,3),
      (4,2),
      (4,1),
      (4,0),
      (3,0),
      (2,0),
      (1,0)
    )

    graphics.setColor(Color.BLACK)
    pixels.foreach { p =>
      graphics.fillRect(p._1,p._2,1,1)
    }
  }
}

object GUI {
  def main(args: Array[String]): Unit = {
    val frame = new JFrame()
    frame.setTitle("SGBC")
    frame.setDefaultCloseOperation(3)
    frame.setLocationRelativeTo(null)
    frame.add(new LCD())
    frame.pack()
    frame.setVisible(true)
  }
}
