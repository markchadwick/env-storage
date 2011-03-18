package env.storage

trait TableFactory[T] {
  def getTable(name: String): Option[Table[T]]
  def createTable(name: String): Table[T]

  def getOrCreateTable(name: String) =
    getTable(name).getOrElse(createTable(name))
}

trait Storage extends TableFactory[Array[Byte]]
trait MapStorage extends TableFactory[Map[String, Array[Byte]]]
