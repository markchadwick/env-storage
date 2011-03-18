package env.storage.hbase

import java.util.UUID

import env.storage.Storage
import env.storage.Table
import env.storage.TableTests

class HBaseTableSpec extends TableTests {
  val storage = LocalCluster.storage

  def name = "HBase Table"

  def withTable(func: Table[Array[Byte]] ⇒ Unit) = {
    val name = UUID.randomUUID().toString()
    func(storage.createTable(name))
  }

  def withStorage(func: Storage ⇒ Unit) = {
    val storage = LocalCluster.storage
    try { func(storage) }
    finally { storage.cleanupTables() }
  }
}
