package env.storage

import scala.collection.SortedMap

/** For most methods, keys may be treated as either byte arrays or strings,
  * though the former is really reccomended. */
trait Table[Value] extends SortedMap[Array[Byte], Value]
            with ByteConversions {

  type Key = Array[Byte]

  def update(key: Key, value: Value): Unit

  def update(key: String, value: Value): Unit =
    update(toBytes(key), value)

  def get(key: String): Option[Value] =
    get(toBytes(key))

  def apply(key: String): Value =
    apply(toBytes(key))

}
