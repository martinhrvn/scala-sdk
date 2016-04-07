lazy val scalacheck = "org.scalacheck" %% "scalacheck" % "1.12.0"
lazy val sprayVersion = "1.3.2"
organization := "com.ibm.watson.developer_cloud"
version := "0.1.0"
scalaVersion := "2.11.7"
crossScalaVersions := Seq("2.10.6", "2.11.7")
name := "scala-sdk"
// add a test dependency on ScalaCheck
libraryDependencies ++= Seq(
  "commons-codec" % "commons-codec" % "1.10",
  "io.spray" %% "spray-json" % sprayVersion,
  "io.spray" %% "spray-client" % sprayVersion,
//  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "ch.qos.logback" % "logback-core" % "1.1.3",
  "com.typesafe.akka" %% "akka-actor" % "2.3.14",
  "joda-time" % "joda-time" % "2.9.3",
  "org.joda" % "joda-convert" % "1.8",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % Test,
  "org.scalatest" %% "scalatest" % "2.2.4" % Test,
  "junit" % "junit" % "4.12" % Test,
  "org.pegdown" % "pegdown" % "1.1.0" % Test)
libraryDependencies ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 11)) => Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0")
    case _ => Seq(
      "com.typesafe"               %% "scalalogging-slf4j"  % "1.0.1")
  }
}