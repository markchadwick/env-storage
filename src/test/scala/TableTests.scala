package env.storage

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
  }

  /** We mix [[ByteConversions]] in to a new object so that no implicits are in
    * play in the test code. */
  object Conv extends ByteConversions
}
