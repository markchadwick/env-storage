package env.storage

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

trait StorageTests extends FlatSpec with ShouldMatchers {
  def name: String
  def withStorage(func: Storage ⇒ Unit)

  behavior of name

  it should "not find a non-existant table" in {
    withStorage { storage ⇒
      storage.getTable("Unknown") should equal (None)
    }
  }

  it should "find an existing table" in {
    withStorage { storage ⇒
      val table = storage.createTable("foobar")
      storage.getTable("foobar") should equal (Some(table))
    }
  }

  it should "return the existing table when getOrCreate'ing" in {
    withStorage { storage ⇒
      val table = storage.createTable("foobar")
      storage.getOrCreateTable("foobar") should be theSameInstanceAs (table)
      storage.getOrCreateTable("another") should not be theSameInstanceAs (table)
    }
  }
}
