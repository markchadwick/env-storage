import sbt._

class EnvStorageProject(info: ProjectInfo) extends ParentProject(info) {

  val storageCore = project("env-storage-core", "env-storage-core",
    new StorageCoreProject(_))

  val hbaseStorage = project("env-storage-hbase", "env-storage-hbase",
    new HbaseStorageProject(_), storageCore)
                          
  class StorageCoreProject(info: ProjectInfo) extends DefaultProject(info) {
    lazy val scalatest = "org.scalatest" % "scalatest" % "1.3" % "test"
    lazy val scalacheck= "org.scala-tools.testing" %% "scalacheck" % "1.8" % "test"
  }

  class HbaseStorageProject(info: ProjectInfo) extends DefaultProject(info) {
    val apacheRepo = "Apache Repo" at "http://repository.apache.org/content/groups/public"
    val rawsonRepo = "Rawson Repo" at "http://people.apache.org/~rawson/repo"
    val javaRepo = "Java Repo" at "http://download.java.net/maven/2"

    lazy val hbase = "org.apache.hbase" % "hbase" % "0.90.0"
    lazy val hbaseTests = "org.apache.hbase" % "hbase-tests" % "0.90.0" % "test" from
      "http://akkimbo.com/hbase-0.90.1-tests.jar"
  }
}
