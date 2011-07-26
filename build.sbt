
organization := "name.heikoseeberger.scamples"

name := "scamples"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.0-1"

libraryDependencies ++= Seq(
    "net.databinder" %% "dispatch-http" % "0.8.4",
    "org.specs2" %% "specs2" % "1.5" % "test")
