package env.storage.memory

import env.storage.Storage


/** In in-memory representation of a sorted keyspace. Like all [[Storage]]
  * implementations, its keys and values are both of type [[Array[Byte]]]. If
  * conversions are needed, they are outside the scope of this implementation.
  *
  * This implementation should largely be used for testing, as there are no
  * limits on the number of tables or their size. It will continue to store
  * values until the heap explodes. */
class MemoryStorage extends Storage {

  /** Internal representation of the created tables and their names. This has no
    * bound on the size, and may explode for large data sets. */
  private var tables = Map.empty[String, MemoryTable]

  /** Optionally get a table with the given name. If the table doesn't exist,
    * this will return [[None]], otherwise a [[Some]] of said table. */
  def getTable(name: String) =
    tables.get(name)

  /** Creates and returns a new table with the given name. If a table already
    * exists with this name, it will be clobbered. */
  def createTable(name: String) = {
    val table = new MemoryTable(name)
    tables += (name → table)
    table
  }
}
