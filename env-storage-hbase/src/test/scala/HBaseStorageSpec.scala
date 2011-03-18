package env.storage.hbase

import scala.collection.mutable.Set

import org.apache.hadoop.hbase.HBaseTestingUtility

import env.storage.ByteConversions
import env.storage.Storage
import env.storage.StorageTests


class HBaseStorageSpec extends StorageTests {
  val util = LocalCluster.util
  val storage = getStorage()

  def name = "HBase Storage"

  def withStorage(func: Storage ⇒ Unit) = {
    try { func(storage) }
    finally { storage.cleanupTables() }
  }

  private def getStorage() = new HBaseStorage {
    val tables = Set.empty[String]

    def newHTable(name: Array[Byte], families: Array[Array[Byte]],
                  numVersions: Int) = {
      util.createTable(name, families, numVersions)
    }

    def cleanupTables() {
      tables.map(ByteConversions.toBytes)
            .foreach(util.truncateTable)
    }
  }
}
