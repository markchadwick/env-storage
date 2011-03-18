package env.storage.hbase

import scala.collection.mutable.Set

import org.apache.hadoop.hbase.HBaseTestingUtility

import env.storage.ByteConversions
import env.storage.Storage
import env.storage.StorageTests


class HBaseStorageSpec extends StorageTests {
  val util = LocalCluster.util

  def name = "HBase Storage"

  def withStorage(func: Storage ⇒ Unit) = {
    val storage = LocalCluster.storage
    try { func(storage) }
    finally { storage.cleanupTables() }
  }
}
