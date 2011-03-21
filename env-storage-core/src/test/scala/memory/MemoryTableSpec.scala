package env.storage.memory

import java.util.UUID

import env.storage.Table
import env.storage.TableTests


class MemoryTableSpec extends TableTests {
  def name = "Memory Table"
  def withTable(func: Table[Array[Byte]] ⇒ Unit) = {
    val name = UUID.randomUUID().toString()
    func(new MemoryTable(name))
  }
}
