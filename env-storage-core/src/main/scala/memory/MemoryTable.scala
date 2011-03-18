package env.storage.memory

import scala.collection.SortedMap

import env.storage.Table


/** In in-memory table which holds a sorted set of keys and their attached
  * values. There are no bounds on the size of this table, so shoving too much
  * in it will very much explode your heap. */
class MemoryTable(name: String) extends Table[Array[Byte]] {

  /** Internal storage which uses [[ByteArrayOrdering]] to sort the keyspace. */
  private var storage = SortedMap.empty[Array[Byte], Array[Byte]](
    new ByteArrayOrdering())

  /** Return a new table that contains the given key. Though this appears to
    * return a copy, this will actually modify the underlying storage. */
  def +[B >: Array[Byte]](kv: (Array[Byte], B)) = storage + kv

  /** Remove a key from the underlying storage. Though this appears to return a
    * copy, it is actually a distructive operation. */
  def -(key: Array[Byte]) = storage - key

  /** Get the ordering of this [[MemoryTable]]. It should always be a
    * [[ByteArrayOrdering]]. The ordering will be based off the key space. */
  def ordering = storage.ordering

  /** Creates an iterator for this [[MemoryTable]]. It will yield each key/value
    * pair in order lexographiclly sorted by the key */
  def iterator = storage.iterator

  /** Optionally get a value from this [[MemoryTable]]. If the given key exists,
    * it will return a [[Some]] of the value, otherwise [[None]]. */
  def get(key: Array[Byte]): Option[Array[Byte]] = storage.get(key)

  /** Get a range of keys and their values where the start and stop positions
    * are optional. If both are [[None]], this will return all key/value pairs
    * in this [[MemoryTable]].
    *
    * @param from   Optional inclusive start key. The beginning of the
    *               [[MemoryTable]] if [[None]].
    * @param until  Optional exclusive end key. The end of the table if
    *               [[None]].
    * @return       An iterator which will yield (key, value) pairs for the
    *               rows of this [[MemoryTable]] falling in the given range.
    */
  def rangeImpl(from: Option[Array[Byte]], until: Option[Array[Byte]]) =
    storage.rangeImpl(from, until)

  /** Update the value of a key in place. If the key is already bound to a
    * value, it will be clobbered. */
  def update(key: Array[Byte], value: Array[Byte]) =
    storage += (key → value)

  /** Tells [[SortedMap]] to display the name of this table when rendering the
    * representation to a [[String]]. */
  override def stringPrefix = name
}
