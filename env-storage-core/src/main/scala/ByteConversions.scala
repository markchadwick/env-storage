package env.storage

import java.{lang ⇒ j}


/** Simple conversersions to and from JVM primatives. Largely based on the
  * [[Bytes]] library from HBase:
  * hbase/src/main/java/org/apache/hadoop/hbase/util/Bytes.java */
object ByteConversions extends ByteConversions {
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


/** An optional mixin to define conversions to and from JVM natives types and
  * their Array[Byte] equivilants. */
trait ByteConversions {
  type BA = Array[Byte]

  /** Byte ordering is lexographic, not numeric. Which is to say that the array
    * [1, 2, 3, 4] is less than [2, 3, 4]. */
  class ByteArrayOrdering extends Ordering[BA] {
    def compare(aa: BA, ab: BA): Int = {
      (aa.headOption, ab.headOption) match {
        case (Some(a), Some(b)) if (a == b) ⇒ compare(aa.tail, ab.tail)
        case (Some(a), Some(b)) if (a > b) ⇒ +1
        case (Some(a), Some(b)) if (a < b) ⇒ -1
        case (None, None) ⇒ 0
        case (None, _) ⇒ -1
        case (_, None) ⇒ +1
        case _ ⇒ 0
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
