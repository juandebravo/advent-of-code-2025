val scala3Version = "3.7.4"

ThisBuild / scalacOptions ++= Seq("-Wnonunit-statement")

lazy val root = project
  .in(file("."))
  .settings(
    name := "AdventOfCode2025",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "co.fs2" %% "fs2-io" % "3.12.2",
      "org.typelevel" %% "weaver-cats" % "0.11.2" % Test,
      "org.typelevel" %% "cats-core" % "2.13.0",
      "org.typelevel" %% "cats-effect" % "3.6.3"
    )
)
