package env.storage

import scala.collection.SortedMap


/** A generic table which has an [[Array[Byte]]] key space, and an associated
  * value. A table will be ordered lexographiclly by its keys. The imlementation
  * must follow this interface as an abstraction, though performance
  * characteristics for querying each underlying type may wildly differ. */
trait Table[T] extends SortedMap[Array[Byte], T]
               with ByteConversions {

  /** Update a key with a value. If the key does not exist, the value will
    * simply be set. If it does, the old value will be clobbered with no
    * notification. */
  def update(key: Array[Byte], value: T): Unit
}
