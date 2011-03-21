package env.storage


/** Interface of a type which may act as a factory for tables. It should simply
  * have the abiltiy to find existing tables and create new ones. Deleting or
  * modifying tables is outside the scope of this interface.
  *
  * For more information on the properties of the returned tables, view the
  * [[Table]] implementation. */
trait TableFactory[T] {

  /** Optionally get a table with the given name. If the table exists, a
    * [[Some]] of that instance will be returned, otherwise [[None]]. */
  def getTable(name: String): Option[Table[T]]

  /** Creates a table with the specified name. The exact schema of the table is
    * up to the implmention. If the specified already exists, this method may
    * throw an exception. */
  def createTable(name: String): Table[T]

  /** Gets a table with the given name. If the table does not exist, it will be
    * created. As noted in the [[createTable]] method, it is up to the
    * implemention to set up the specifics of the optionally created table. */
  def getOrCreateTable(name: String) =
    getTable(name).getOrElse(createTable(name))
}


/** Defines a [[Storage]] which can manage a set of one dimensional tables. */
trait Storage extends TableFactory[Array[Byte]]


/** Defines a [[Storage]] which can manage a set of two dimensional tables */
trait MapStorage extends TableFactory[Map[String, Array[Byte]]]
