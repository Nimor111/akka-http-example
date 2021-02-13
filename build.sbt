import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "todo"
ThisBuild / organizationName := "todo"

lazy val root = (project in file("."))
  .settings(
    name := "todo-akka-http",
    libraryDependencies ++= Seq(
    	scalaTest % Test,
	"com.typesafe.akka" %% "akka-http"   % "10.1.10" ,
	"com.typesafe.akka" %% "akka-stream" % "2.5.25",
	"com.typesafe.akka" %% "akka-http-spray-json" % "10.1.10",
	"com.typesafe.akka" %% "akka-testkit" % "2.5.25" % "test",
	"org.specs2" %% "specs2-core" % "4.6.0" % "test"
    )
  )
