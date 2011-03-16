import sbt._

class EnvStorageProject(info: ProjectInfo) extends DefaultProject(info) {
  lazy val scalatest = "org.scalatest" % "scalatest" % "1.3" % "test"
  lazy val scalacheck= "org.scala-tools.testing" %% "scalacheck" % "1.8" % "test"
}
