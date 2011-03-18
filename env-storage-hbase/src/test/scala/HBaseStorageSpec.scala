package env.storage.hbase

import env.storage.Storage
import env.storage.StorageTests


class HBaseStorageSpec extends StorageTests {
  def name = "HBase Storage"
  def withStorage(func: Storage ⇒ Unit) = ()
}
