import sbt._

class EnvStorageProject(info: ProjectInfo) extends ParentProject(info) {

  val storageCore = project("env-storage-core", "env-storage-core",
    new StorageCoreProject(_))

  val hbaseStorage = project("env-storage-hbase", "env-storage-hbase",
    new HbaseStorageProject(_), storageCore)
                          
  class StorageCoreProject(info: ProjectInfo) extends DefaultProject(info) {
    lazy val scalatest = Dependencies.scalatest % "test"
    lazy val scalacheck = Dependencies.scalacheck % "test"
  }

  class HbaseStorageProject(info: ProjectInfo) extends DefaultProject(info) {
    val apacheRepo = "Apache Repo" at "http://repository.apache.org/content/groups/public"

    lazy val hbase = Dependencies.hbase
    lazy val hbaseTests = Dependencies.hbaseTests
    lazy val hdfsTest = Dependencies.hadoopTest % "test"
    lazy val slf4j = Dependencies.slf4j % "test"
  }

  object Dependencies {
    def hadoopTest = "org.apache.hadoop" % "hadoop-test" % "0.20.2"
    def hbase = "org.apache.hbase" % "hbase" % "0.90.2"
    def hbaseTests = "org.apache.hbase" % "hbase-tests" % "0.90.2" % "test" from
      "https://repository.apache.org/content/groups/public/org/apache/hbase/hbase/0.90.2/hbase-0.90.2-tests.jar"
    def scalacheck= "org.scala-tools.testing" %% "scalacheck" % "1.8"
    def scalatest = "org.scalatest" % "scalatest" % "1.3"
    def slf4j = "org.slf4j" % "slf4j-api" % "1.5.5"
  }
}
