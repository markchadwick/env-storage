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

      tableSet(table, "one", "two")
      tableSet(table, "two", "four")

      tableGet(table, "one") should equal (Some("two"))
      table(Conv.toBytes("two")) should equal (Conv.toBytes("four"))

      intercept[NoSuchElementException] {
        table(Conv.toBytes("three"))
      }
    }
  }

  it should "have a lexographiclly sorted keyspace" in {
    withTable { table ⇒
      val ordering = new Conv.ByteArrayOrdering()
      (1 to 100) foreach { i ⇒
        val key = UUID.randomUUID.toString
        tableSet(table, key, "x")
      }

      // Ensure Lexo Ording
      tableSet(table, "bar1", "foo1")
      tableSet(table, "bar2", "foo2")
      tableSet(table, "bar3", "foo3")

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

  it should "have an iterator which returns all rows" in {
    withTable { table ⇒
      List("aaa", "bbb", "ccc", "ddd")
        .sorted(new ShuffleOrdering)
        .map(ByteConversions.toBytes)
        .foreach(v ⇒ table(v) = v)
      
      val iterator = table.iterator

      iterator.hasNext should be (true)
      Conv.toUtf8(iterator.next._1) should equal ("aaa")

      iterator.hasNext should be (true)
      Conv.toUtf8(iterator.next._1) should equal ("bbb")

      iterator.hasNext should be (true)
      Conv.toUtf8(iterator.next._1) should equal ("ccc")

      iterator.hasNext should be (true)
      Conv.toUtf8(iterator.next._1) should equal ("ddd")

      iterator.hasNext should be (false)
    }
  }

  it should "find a range inclusive of the first key exclusive of the last" in {
    withTable { table ⇒
      val vals = List("bbb", "ccc", "ddd", "eee")
                  .sorted(new ShuffleOrdering)
                  .map(ByteConversions.toBytes)
                  .foreach(v ⇒ table(v) = v)

      val r1 = table.range(Conv.toBytes("ccc"), Conv.toBytes("eee"))
      r1 should have size (2)
      toStrings(r1.keys) should equal (List("ccc", "ddd"))

      val r2 = table.range(Conv.toBytes("ddd"), Conv.toBytes("zzz"))
      r2 should have size (2)
      toStrings(r2.keys) should equal (List("ddd", "eee"))

      val r3 = table.range(Conv.toBytes("aaa"), Conv.toBytes("bbb"))
      r3 should have size (0)

      val r4 = table.range(Conv.toBytes("aaa"), Conv.toBytes("ddd"))
      r4 should have size (2)
      toStrings(r4.keys) should equal (List("bbb", "ccc"))
    }
  }

  it should "query from a given key until the end of the table" in {
    withTable { table ⇒
      val vals = List("aaa", "bbb", "ccc", "ddd")
                  .sorted(new ShuffleOrdering)
                  .map(ByteConversions.toBytes)
                  .foreach(v ⇒ table(v) = v)

      val r1 = table.from(Conv.toBytes("ccc"))
      r1 should have size (2)
      toStrings(r1.keys) should equal (List("ccc", "ddd"))

      val r2 = table.from(Conv.toBytes("ddd"))
      r2 should have size (1)
      toStrings(r2.keys) should equal (List("ddd"))

      val r3 = table.from(Conv.toBytes("eee"))
      r3 should have size (0)

      val r4 = table.from(Conv.toBytes("aaa"))
      r4 should have size (4)
    }
  }

  it should "remove a key" in {
    withTable { table ⇒
      tableSet(table, "one", "one")
      tableSet(table, "two", "two")
      tableSet(table, "three", "three")
      tableSet(table, "four", "one")

      tableGet(table, "one") should equal (Some("one"))
      tableGet(table, "three") should equal (Some("three"))

      table - Conv.toBytes("three")

      pending
    }
  }

  it should "clobber a value on update" is (pending)

  /** We mix [[ByteConversions]] in to a new object so that no implicits are in
    * play in the test code. */
  object Conv extends ByteConversions

  class ShuffleOrdering extends Ordering[Any] {
    def compare(a: Any, b: Any) =
      (scala.math.random * 3).toInt - 1
  }

  private def tableSet(table: Table[Array[Byte]], key: String, value: String) =
    table(Conv.toBytes(key)) = Conv.toBytes(value)

  private def tableGet(table: Table[Array[Byte]], key: String) =
    table.get(Conv.toBytes(key)).map(Conv.toUtf8)

  private def toStrings(s: Traversable[Array[Byte]]): List[String] =
    s.map(Conv.toUtf8).toList
}
