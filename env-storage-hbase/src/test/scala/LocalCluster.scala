package env.storage.hbase

import org.apache.hadoop.hbase.HBaseTestingUtility
import org.apache.hadoop.hbase.client.HTable
import org.apache.log4j.Level
import org.apache.log4j.Logger

import env.storage.ByteConversions


object LocalCluster {
  lazy val util = getHBaseUtil
  lazy val storage = getStorage

  private def getStorage() = new HBaseStorage {
    val tables = Set.empty[String]

    def newHTable(name: Array[Byte], families: Array[Array[Byte]],
                  numVersions: Int) = {
      util.createTable(name, families, numVersions)
    }

    def cleanupTables() {
      tables.map(ByteConversions.toBytes)
            .foreach(util.truncateTable)
    }
  }

  private def getHBaseUtil: HBaseTestingUtility = {
    println("- " * 30)
    println("- Starting HBase Cluster...")
    println("- " * 30)

    // Make things a touch less chatty
    Logger.getRootLogger.setLevel(Level.INFO)

    val util = new HBaseTestingUtility()
    util.startMiniCluster

    Runtime.getRuntime().addShutdownHook(new Thread {
      override def run = {
        println("- " * 30)
        println("- Stopping HBase Cluster...")
        println("- " * 30)

        util.shutdownMiniCluster()
      }
    })

    return util
  }

}
