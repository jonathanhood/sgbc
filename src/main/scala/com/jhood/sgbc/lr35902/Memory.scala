package com.jhood.sgbc.lr35902

trait Memory {
  def fetch(address: Short): Byte
  def fetchShort(address: Short): Byte
}
