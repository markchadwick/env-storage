package env.storage

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers


class ByteArrayOrderingSpec extends FlatSpec with ShouldMatchers {
  behavior of "Byte Array Ordering"

  protected val ordering = new ByteConversions.ByteArrayOrdering()
  protected def compare(a: Array[Byte], b: Array[Byte]) =
    ordering.compare(a, b)

  it should "determine if two arrays are equal" in {
    val a = Array[Byte](1, 2, 3, 4)
    val b = Array[Byte](1, 2, 3, 4)
    compare(a, b) should equal (0)
  }

  it should "determine if one array is larger than the other" in {
    val a = Array[Byte](2, 3, 4, 5)
    val b = Array[Byte](1, 2, 3, 4)
    compare(a, b) should be > (0)
  }

  it should "determine if one array is smaller than the other" in {
    val a = Array[Byte](1, 2, 3, 4)
    val b = Array[Byte](2, 3, 4, 5)
    compare(a, b) should be < (0)
  }

  it should "determine size from the head" in {
    val a = Array[Byte](2)
    val b = Array[Byte](1, 2, 3, 4)

    compare(a, b) should be > (0)
  }

  it should "consider a longer array larger" in {
    val a = Array[Byte](1, 2, 3, 4)
    val b = Array[Byte](1, 2, 3, 4, 5)
    compare(a, b) should be < (0)
  }

  it should "consider an empty array small" in {
    val a = Array[Byte]()
    val b = Array[Byte](-100)

    compare(a, b) should be < (0)
  }
}
