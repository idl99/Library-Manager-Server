name := """Library-Manager-Server"""
organization := "com.ihandilnath"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean)

scalaVersion := "2.12.6"

libraryDependencies += guice
libraryDependencies += javaJdbc
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.6"
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"
libraryDependencies += evolutions