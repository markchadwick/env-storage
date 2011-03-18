package env.storage.hbase

import org.apache.hadoop.hbase.HBaseTestingUtility

object HBaseCluster {
  lazy val hbaseUtil = getHBaseUtil

  def shutdown = {
    println("- " * 30)
    println("- Stopping HBase Cluster...")
    println("- " * 30)
    hbaseUtil.shutdownMiniCluster()
  }

  private def getHBaseUtil = {
    println("- " * 30)
    println("- Starting HBase Cluster...")
    println("- " * 30)
    val util = new HBaseTestingUtility()
    util.startMiniCluster
    util
  }

  Runtime.getRuntime().addShutdownHook(new Thread {
    override def run = shutdown
  })
}
