package env.storage.memory

import env.storage.Storage


class MemoryStorage extends Storage {
  private var tables = Map.empty[String, MemoryTable]

  def getTable(name: String) =
    tables.get(name)

  def createTable(name: String) = {
    val table = new MemoryTable(name)
    tables += (name → table)
    table
  }

  override def toString = tables.toString
}
