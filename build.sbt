resolvers in ThisBuild ++= Seq(
  Resolver.typesafeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
  "rossabaker Bintray Repo" at "http://dl.bintray.com/rossabaker/maven"
)

publishMavenStyle in ThisBuild := true

scalaVersion in ThisBuild := "2.11.4"

organization in ThisBuild := "com.nokia.bundles"

autoScalaLibrary in ThisBuild := false

publishTo in ThisBuild := Some(Resolver.file("file", new File("./deploy")))

scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation", "-feature")
