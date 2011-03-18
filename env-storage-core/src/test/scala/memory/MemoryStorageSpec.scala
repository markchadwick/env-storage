package env.storage.memory

import env.storage.Storage
import env.storage.StorageTests

class MemoryStorageSpec extends StorageTests {
  def name = "Memory Storage"
  def withStorage(func: Storage ⇒ Unit) =
    func(new MemoryStorage())
}
