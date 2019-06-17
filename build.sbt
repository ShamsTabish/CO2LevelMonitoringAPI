name := """Co2_Status"""
organization := "tabish.in"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.2" % Test
libraryDependencies += "com.h2database" % "h2" % "1.4.192"
libraryDependencies += "org.mockito" % "mockito-core" % "2.25.1" % Test
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.16"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "tabish.in.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "tabish.in.binders._"
