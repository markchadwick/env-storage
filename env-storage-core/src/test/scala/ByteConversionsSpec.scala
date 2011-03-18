package env.storage

import org.scalacheck.Arbitrary._
import org.scalacheck.Prop._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.Checkers

object Conversions extends ByteConversions
import Conversions._


class ByteConversionsSpec extends FlatSpec
                          with ShouldMatchers
                          with Checkers {

  behavior of "Byte Conversions"

  it should "convert a long" in {
    def flip(long: Long) = toLong(toBytes(long))

    flip(0) should equal (0)
    flip(1) should equal (1)
    flip(-1) should equal (-1)

    val checks = math.pow(2, 8).asInstanceOf[Long] ::
                math.pow(2, 16).asInstanceOf[Long] ::
                math.pow(2, 24).asInstanceOf[Long] ::
                math.pow(2, 32).asInstanceOf[Long] ::
                Nil
    checks foreach { check ⇒
      flip(check) should equal (check)
      flip(-check) should equal (-check)
    }

    check((long: Long) ⇒ flip(long) == long)
  }

  it should "convert a double" in {
    def flip(double: Double) = toDouble(toBytes(double))
    check((double: Double) ⇒ flip(double) == double)
  }

  it should "convert an int" in {
    def flip(int: Int) = toInt(toBytes(int))

    flip(0) should equal (0)
    flip(1) should equal (1)
    flip(-1) should equal (-1)

    check((int: Int) ⇒ flip(int) == int)
  }

  it should "convert a float" in {
    def flip(float: Float) = toFloat(toBytes(float))
    check((float: Float) ⇒ flip(float) == float)
  }
}
