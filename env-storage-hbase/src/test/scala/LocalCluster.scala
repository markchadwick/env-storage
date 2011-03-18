package env.storage.hbase

import org.apache.hadoop.hbase.HBaseTestingUtility
import org.apache.hadoop.hbase.client.HTable

import env.storage.ByteConversions


object LocalCluster {
  lazy val util = getHBaseUtil

  private def getHBaseUtil: HBaseTestingUtility = {
    println("- " * 30)
    println("- Starting HBase Cluster...")
    println("- " * 30)

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
