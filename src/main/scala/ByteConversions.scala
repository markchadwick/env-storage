package env.storage

import java.{lang ⇒ j}

/** Largely stolen from:
  * hbase/src/main/java/org/apache/hadoop/hbase/util/Bytes.java
  */
object ByteConversions {
  val SIZEOF_BOOLEAN = j.Byte.SIZE / j.Byte.SIZE
  val SIZEOF_BYTE = j.Byte.SIZE
  val SIZEOF_CHAR = j.Character.SIZE / j.Byte.SIZE
  val SIZEOF_DOUBLE = j.Double.SIZE / j.Byte.SIZE
  val SIZEOF_FLOAT = j.Float.SIZE / j.Byte.SIZE
  val SIZEOF_INT = j.Integer.SIZE / j.Byte.SIZE
  val SIZEOF_LONG = j.Long.SIZE / j.Byte.SIZE
  val SIZEOF_SHORT = j.Short.SIZE / j.Byte.SIZE
}

import ByteConversions._

trait ByteConversions {
  type BA = Array[Byte]

  class ByteArrayOrdering extends Ordering[BA] {
    def compare(a: BA, b: BA): Int = {
      if(a.length > b.length) {
        return -1
      } else if (b.length > a.length) {
        return 1
      } else {
        a zip b foreach { case(ba, bb) ⇒
          if(ba > bb) return -1
          if(bb > ba) return 1
        }
        return 0
      }
    }
  }

  def toUtf8(ba: BA) = new String(ba, "UTF-8")

  def toDouble(ba: BA) =
    j.Double.longBitsToDouble(toLong(ba, 0, SIZEOF_LONG))

  def toFloat(ba: BA) =
    j.Float.intBitsToFloat(toInt(ba, 0, SIZEOF_INT))

  def toLong(ba: BA): Long = toLong(ba, 0, SIZEOF_LONG)
  def toLong(bytes: BA, offset: Int, length: Int): Long = {
    if(length != SIZEOF_LONG || offset + length > bytes.length) {
      throw new IllegalArgumentException("Bad Parameters")
    }
    var long = 0L
    (offset to (offset + length - 1)) foreach { i ⇒
      long <<= 8
      long ^= bytes(i) & 0xFF
    }
    return long
  }

  def toInt(ba: BA): Int = toInt(ba, 0, SIZEOF_INT)

  def toInt(bytes: BA, offset: Int, length: Int): Int = {
    if(length != SIZEOF_INT || offset + length > bytes.length) {
      throw new IllegalArgumentException("Bad Parameters")
    }
    var int: Int = 0
    (offset to (offset + length - 1)) foreach { i ⇒
      int <<= 8
      int ^= bytes(i) & 0xFF
    }
    return int
  }

  def toBytes(s: String): BA = s.getBytes("UTF-8")

  def toBytes(d: Double): BA =
    toBytes(j.Double.doubleToRawLongBits(d))

  def toBytes(lval: Long): BA = {
    var long = lval
    val b = new Array[Byte](SIZEOF_LONG)
    7.to(1, -1) foreach { i ⇒
      b(i) = long.asInstanceOf[Byte]
      long >>>= 8
    }
    b(0) = long.asInstanceOf[Byte]
    return b 
  }

  def toBytes(intVal: Int): BA = {
    var int = intVal
    val bytes = new Array[Byte](SIZEOF_INT)
    3.to(1, -1) foreach { i ⇒
      bytes(i) = int.asInstanceOf[Byte]
      int >>>= 8
    }
    bytes(0) = int.asInstanceOf[Byte]
    return bytes
  }

  def toBytes(float: Float): BA = {
    toBytes(j.Float.floatToRawIntBits(float))
  }
}
