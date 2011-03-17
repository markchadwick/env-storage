package env.storage

import java.util.UUID

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers


trait TableTests extends FlatSpec with ShouldMatchers {
  def name: String
  def withTable(func: Table[Array[Byte]] ⇒ Unit)

  behavior of name

  it should "store a value to a key" in {
    withTable { table ⇒
      val two = Conv.toBytes("two")
      val four = Conv.toBytes("four")
      table("one") = two
      table("two") = four

      table.get("one") should equal (Some(two))
      table("two") should equal (four)

      intercept[NoSuchElementException] {
        table("three")
      }
    }
  }

  it should "have a sortable keyspace" in {
    withTable { table ⇒
      val ordering = new Conv.ByteArrayOrdering()
      (1 to 100) foreach { i ⇒
        val key = UUID.randomUUID.toString
        val keyBytes = Conv.toBytes(key)
        table(keyBytes) = Conv.toBytes("x")
      }

      table(Conv.toBytes("bar1")) = Conv.toBytes("foo1")
      table(Conv.toBytes("bar2")) = Conv.toBytes("foo2")
      table(Conv.toBytes("bar3")) = Conv.toBytes("foo3")

      val none = (Array.empty[Byte], Array.empty[Byte])
      table.foldLeft(none)((prev, next) ⇒ {
        val prevKey = prev._1
        val nextKey = next._1

        if(prev != none) {
          ordering.compare(prevKey, nextKey) should be < (0)
        }
        next
      })
    }
  }

  /** We mix [[ByteConversions]] in to a new object so that no implicits are in
    * play in the test code. */
  object Conv extends ByteConversions
}
