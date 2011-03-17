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

  it should "compare simple Byte Arrays" in {
    val ord = new ByteArrayOrdering()
    
    ord.compare(Array(0), Array(0)) should equal (0)
    ord.compare(Array(0), Array(1)) should be < (0)
    ord.compare(Array(1), Array(0)) should be > (0)
  }

  it should "compare long byte arrays" in {
    val ord = new ByteArrayOrdering()

    val arrayA = Array[Byte](0, 0, 0)
    val arrayB = Array[Byte](0, 0, 1)
    val arrayC = Array[Byte](0, 1, 0)
    val arrayD = Array[Byte](1, 0, 1)

    ord.compare(arrayA, arrayA) should equal (0)
    ord.compare(arrayD, arrayD) should equal (0)

    ord.compare(arrayA, arrayB) should be < (0)
    ord.compare(arrayB, arrayA) should be > (0)
    ord.compare(arrayB, arrayC) should be < (0)
    ord.compare(arrayC, arrayD) should be < (0)
  }

  it should "compare differnt sized byte arrays" in {
    val ord = new ByteArrayOrdering()

    val arrayA = Array[Byte](1, 0, 0)
    val arrayB = Array[Byte](3)

    ord.compare(arrayA, arrayB) should be > (0)
    ord.compare(arrayB, arrayA) should be < (0)
  }

  /*
  it should "convert a string" in {
    check((string: String) ⇒ {
      val flip = toUtf8(toBytes(string))
      println("'%s' == '%s' %s".format(string, flip, (string == flip)))
      flip == string
    })
  }
  */

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
