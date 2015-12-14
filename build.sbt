// factor out common settings into a sequence
lazy val commonSettings = Seq(
  organization := "org.myproject",
  version := "0.1.0",
  scalaVersion := "2.11.5"
)

// define ModuleID for library dependencies
lazy val scalacheck = "org.scalacheck" %% "scalacheck" % "1.12.0"
lazy val sprayVersion = "1.3.2"
lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "My Project",
    // add a test dependency on ScalaCheck
    libraryDependencies ++= Seq(
      "commons-codec" % "commons-codec" % "1.10",
      "io.spray" %% "spray-json" % sprayVersion,
      "io.spray" %% "spray-client" % sprayVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
      "ch.qos.logback" % "logback-classic" % "1.1.3",
      "ch.qos.logback" % "logback-core" % "1.1.3",
      "com.typesafe.akka" %% "akka-actor" % "2.3.14",
      "org.scalatest" %% "scalatest" % "2.2.4" % Test,
      "junit" % "junit" % "4.12" % Test,
      "org.pegdown" % "pegdown" % "1.1.0" % Test

    )
  )
