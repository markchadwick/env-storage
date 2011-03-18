package env.storage.hbase

import org.apache.hadoop.hbase.client.Delete
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.client.HTable
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.client.Scan

import env.storage.Table

class HBaseTable(name: String, hTable: HTable,
                 from: Option[Array[Byte]], until: Option[Array[Byte]])
      extends Table[Array[Byte]] {

  def this(name: String, hTable: HTable) =
    this(name, hTable, None, None)

  private val MIN_KEY = Array.empty[Byte]
  private val COL = HBaseStorage.familyName

  def +[T >: Array[Byte]](kv: (Array[Byte], T)): this.type = {
    val value = kv._2.asInstanceOf[Array[Byte]]
    hTable.put(new Put(kv._1).add(COL, COL, value))
    this
  }

  /** This won't necessarily delete the row, it will simply delete the values of
    * the column. @TODO: not sure this is correct. */
  def -(key: Array[Byte]): this.type = {
    hTable.delete(new Delete(key).deleteColumn(COL, COL))
    this
  }
  
  def update(key: Array[Byte], value: Array[Byte]) =
    this + (key → value)

  def ordering = new ByteArrayOrdering()
  def iterator = {
    val min = from.getOrElse(MIN_KEY)
    val scan = until match {
      case None ⇒ new Scan(min)
      case Some(max) ⇒ new Scan(min, max)
    }
    scan.addColumn(COL, COL)
    val scanner = hTable.getScanner(scan)

    val chunk = 10
    Stream.continually(scanner.next(chunk))
          .takeWhile(_.size > 0)
          .flatten
          .map(res ⇒ (res.getRow, res.value))
          .toIterator
  }

  def get(key: Array[Byte]): Option[Array[Byte]] = {
    val get = new Get(key).addColumn(COL, COL)
    val result = hTable.get(get)
    if(result.isEmpty()) None
    else Some(result.value())
  }

  def rangeImpl(from: Option[Array[Byte]], until: Option[Array[Byte]]) =
    new HBaseTable(name, hTable, from, until)
}
