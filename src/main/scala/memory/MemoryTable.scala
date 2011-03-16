package env.storage.memory

import scala.collection.SortedMap

import env.storage.Table


class MemoryTable(name: String) extends Table[Array[Byte]] {
  type Value = Array[Byte]

  private var storage = SortedMap.empty[Key, Value](new ByteArrayOrdering())

  def +[T >: Key](kv: (Key, T)) = storage + kv
  def -(key: Key) = storage - key
  def ordering = storage.ordering
  def iterator = storage.iterator
  def get(key: Key): Option[Value] = storage.get(key)

  def rangeImpl(from: Option[Key], until: Option[Key]) =
    storage.rangeImpl(from, until)

  def update(key: Key, value: Value) =
    storage += (key → value)

  override def stringPrefix = name
}
