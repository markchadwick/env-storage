package env.storage.memory

import env.storage.MapStorage
import env.storage.Storage
import env.storage.TableFactory


/** In in-memory representation of a sorted keyspace. Like all [[Storage]]
  * implementations, its keys and values are both of type [[Array[Byte]]]. If
  * conversions are needed, they are outside the scope of this implementation.
  *
  * This implementation should largely be used for testing, as there are no
  * limits on the number of tables or their size. It will continue to store
  * values until the heap explodes. */
trait InMemoryTables[T] extends TableFactory[T] {
  def initialTables: Map[String, MemoryTable[T]]

  /** Internal representation of the created tables and their names. This has no
    * bound on the size, and may explode for large data sets. */
  private var tables = initialTables

  /** Optionally get a table with the given name. If the table doesn't exist,
    * this will return [[None]], otherwise a [[Some]] of said table. */
  def getTable(name: String) =
    tables.get(name)

  /** Creates and returns a new table with the given name. If a table already
    * exists with this name, it will be clobbered. */
  def createTable(name: String) = {
    val table = new MemoryTable[T](name)
    tables += (name → table)
    table
  }
}

class MemoryStorage extends Storage with InMemoryTables[Array[Byte]] {
  def initialTables = Map.empty[String, MemoryTable[Array[Byte]]]
}

class MemoryMapStorage extends MapStorage
                       with InMemoryTables[Map[String, Array[Byte]]] {
  def initialTables = Map.empty[String, MemoryTable[Map[String, Array[Byte]]]]
}
