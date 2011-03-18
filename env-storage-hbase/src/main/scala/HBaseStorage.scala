package env.storage.hbase

import org.apache.hadoop.hbase.client.HTable

import env.storage.ByteConversions
import env.storage.Storage

/** Basic HBase 1d storage implementation. This trait will define everything
  * about how the storage layer behaves except for how to create a table (for
  * easy of testing). Along those lines, it may be subclassed overrideing
  * [[initialTables]]. Otherwise, it will assume the cluster has no HTables. */
trait HBaseStorage extends Storage {

  /** Keep a local cache of the tables so that we don't have to query the
    * underlying API every time someone asks for one. */
  private var tables = Map.empty[String, HBaseTable]

  /** The default family name. Because this is a one-dimensional store on top of
    * a multi-dimensional store, we have to pick an arbitary, consistant name so
    * we know which dimension to chunk values in. */
  private var familyName = ByteConversions.toBytes("VALUE")

  /** Abtract method to create a [[HTable]] with the following parameters. The
    * implementing method need to worry about the table already existing -- this
    * class should take care of that. */
  def newHTable(name: Array[Byte], families: Array[Array[Byte]],
                numVersions: Int): HTable

  /** Tables which exist in the HBase storage at the time this class was
    * initialized. */
  def initialTables: Seq[HTable] = Nil

  /** Optionally get a table with the given name. For the moment, this will go
    * straight to the cache (as it assumes [[initialTables]] was populated. */
  def getTable(name: String) = tables.get(name)

  /** Create a new table with the given name. If the table appears to already
    * exist, either through population by [[initialTables]] or creating a table
    * with this [[Storage]], it will return the same instance. Otherwise, the
    * table will be created with parameters of this implementation's choosing.
    */
  def createTable(name: String): HBaseTable = {
    tables.get(name) match {
      case Some(table) ⇒ table
      case None ⇒
        val tableName = ByteConversions.toBytes(name)
        val hTable = newHTable(tableName, Array(familyName), 1)
        val hBaseTable = hTableToHBaseTable(hTable)

        tables += (name → hBaseTable)
        hBaseTable
    }
  }

  /** Utility method to convert an HBase [[HTable]] to an [[HBaseTable]], which
    * is the class used locally. */
  protected def hTableToHBaseTable(hTable: HTable): HBaseTable = {
    val name = ByteConversions.toUtf8(hTable.getTableName)
    new HBaseTable(name, hTable)
  }
}
