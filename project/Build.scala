import sbt._
import Keys._
import com.typesafe.sbt.osgi.SbtOsgi._


case class BundleDesc(symbolicName: String, deps: Seq[ModuleID], importPackage: Seq[String] = List("*"),
                      exportPackage: Seq[String] = List("*"), fragmentHost: Option[String] = None,
                      headers: Map[String, String] = Map())

case class SourceDesc(symbolicName: String, deps: Seq[ModuleID], version: Option[String] = None,
                      exportPackage: Seq[String] = List("*"))


object BundlifyBuild extends Build {
  val rhoVersion = "0.2.0"
  val http4sVersion = "0.4.2"
  val specsVersion = "3.0-M1"
  val scalazVersion = "7.1.0"
  val spireVersion = "0.8.2"
  val monocleVersion = "0.5.1"
  val blazeVersion = "0.3.0"
  val npnVersion = "8.1.2.v20120308"
  val esVersion = "0.1.3"
  val httpCoreVersion = "4.3.2"
  val kafkaVersion = "0.8.2-beta"
  val cassandraVersion = "2.1.2"

  val scalaSuffix = "_2.11"


  val scalaParserCombinatorImport = "scala.util.parsing.*;version=${@}"
  val scalaXmlImport = "scala.xml.*;version=\"[1.0, 1.1)\""
  val scalaImport = "scala.*;version=\"[2.11, 2.12)\""
  val all = "*"

  //common imports
  val sbtImport = List("org.scalatools.testing;version=\"[1.0, 2)\";resolution:=optional",
                       "sbt.testing;version=\"[1.0, 2)\";resolution:=optional")
  // common Exports
  val orgSpecs2 = "org.specs2;-split-package:=first"
  val specs2 = "specs2;-split-package:=first"
  val orgSpecs2Mutable = "org.specs2.mutable.*;-split-package:=first"
  val orgSpecs2Runner = "org.specs2.runner.*;-split-package:=first"
  val orgSpecs2Spec = "org.specs2.specification.*;-split-package:=first"
  val orgSpecs2Reporter = "org.specs2.reporter.*;-split-package:=first"
  val orgSpecs2Matcher = "org.specs2.matcher.*;-split-package:=first"

  val specs2Host = Some("org.specs2.core")

  // List("sbt.testing;resolution:=optional", "org.scalatools.testing;resolution:=optional", "*")
  lazy val bundleToWrap = List(
    // Test frameworks
    BundleDesc("org.specs2",
               List("org.specs2" %% "specs2-core" % specsVersion,
               "org.specs2" %% "specs2-junit" % specsVersion,
               "org.specs2" %% "specs2-common" % specsVersion,
               "org.specs2" %% "specs2-matcher" % specsVersion,
               "org.specs2" %% "specs2-mock" % specsVersion,
               "org.specs2" %% "specs2-scalacheck" % specsVersion),
               exportPackage = List("org.specs2.*;-split-package:=merge-first", "specs2.*;-split-package:=merge-first"),
               importPackage = sbtImport :+ all,
               headers = Map("Eclipse-BuddyPolicy" -> "dependent", "Bundle-ActivationPolicy" -> "lazy")),

    BundleDesc("org.scalacheck", "org.scalacheck" %% "scalacheck" % "1.12.1",
               exportPackage = List("org.scalacheck.*;-split-package:=first"),
               importPackage = sbtImport :+ all),

    BundleDesc("org.http4s.rho", "org.http4s" %% "rho-core" % rhoVersion,
               exportPackage = List("org.http4s.rho.*")),

    BundleDesc("org.http4s.core", "org.http4s" %% "http4s-core" % http4sVersion :: Nil),
    BundleDesc("org.http4s.dsl", "org.http4s" %% "http4s-dsl" % http4sVersion :: Nil),
    BundleDesc("org.http4s.client", "org.http4s" %% "http4s-client" % http4sVersion :: Nil),
    BundleDesc("org.http4s.server", "org.http4s" %% "http4s-server" % http4sVersion :: Nil, fragmentHost = Some("org.http4s.core")),
    BundleDesc("org.http4s.websocket", "org.http4s" %% "http4s-websocket" % "0.1.1" :: Nil, fragmentHost = Some("org.http4s.core")),
    BundleDesc("org.http4s.blazeserver", "org.http4s" %% "http4s-blazeserver" % http4sVersion :: Nil),
    BundleDesc("org.http4s.blazeclient", "org.http4s" %% "http4s-blazeclient" % http4sVersion :: Nil),
    BundleDesc("org.http4s.blazecore", "org.http4s" %% "http4s-blazecore" % http4sVersion :: Nil),
    BundleDesc("org.http4s.blaze.http", "org.http4s" %% "blaze-http" % blazeVersion :: Nil, fragmentHost = Some("org.http4s.blazecore")),
    BundleDesc("org.http4s.blaze", "org.http4s" %% "blaze-core" % blazeVersion :: Nil, fragmentHost = Some("org.http4s.blazecore")),
    BundleDesc("org.http4s.argonaut", "org.http4s" %% "http4s-argonaut" % http4sVersion :: Nil),

    BundleDesc("io.argonaut", "io.argonaut" %% "argonaut" % "6.1-M4" :: Nil),
    BundleDesc("org.eclipse.jetty.npn.api", "org.eclipse.jetty.npn" % "npn-api" % npnVersion :: Nil),
    BundleDesc("org.eclipse.jetty.npn.boot", "org.mortbay.jetty.npn" % "npn-boot" % npnVersion :: Nil,
              "sun.*;resolution:=optional" :: "*" :: Nil),
    BundleDesc("net.iharder.base64", "net.iharder" % "base64" % "2.3.8" :: Nil),
    BundleDesc("org.log4s", "org.log4s" %% "log4s" % "1.1.2" :: Nil),
    BundleDesc("org.parboiled", "org.parboiled" %% "parboiled" % "2.0.0" :: Nil),
    BundleDesc("org.scalatra.rl", "org.scalatra.rl" %% "rl" % "0.4.10" :: Nil),

    BundleDesc("org.spire",
               "org.spire-math" %% "spire" % spireVersion ::
               "org.spire-math" %% "spire-macros" % spireVersion :: Nil),
    BundleDesc("org.typelevel.machinist", "org.typelevel" %% "machinist" % "0.3.0" :: Nil),

    BundleDesc("monocle",
               "com.github.julien-truffaut" %% "monocle-core" % monocleVersion ::
               "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion ::
               "com.github.julien-truffaut" %% "monocle-generic" % monocleVersion :: Nil,
               """scala.reflect.macros.contexts;version="[2.11, 2.12)";resolution:=optional""" ::
               """scala.tools.nsc.*;version="[2.11, 2.12)";resolution:=optional""" :: "*" :: Nil),

    BundleDesc("jedis", "redis.clients" % "jedis" % "2.6.0" :: Nil),

    BundleDesc("io.searchbox.jest", "io.searchbox" % "jest" % esVersion :: "io.searchbox" % "jest-common" % esVersion :: Nil),

    BundleDesc("org.apache.httpcomponents.httpcore.nio", "org.apache.httpcomponents" % "httpcore-nio" % httpCoreVersion :: Nil),

    BundleDesc("org.apache.kafka.clients", "org.apache.kafka" % "kafka-clients" % kafkaVersion :: Nil,
               """net.jpountz.*;version="[1.2, 2.0)";resolution:=optional""" :: "*" :: Nil),
    BundleDesc("org.apache.kafka.core", "org.apache.kafka" %% "kafka" % kafkaVersion :: Nil,
               "com.yammer.metrics;resolution:=optional" ::
               "com.yammer.metrics.core;resolution:=optional" ::
               "com.yammer.metrics.reporting;resolution:=optional" ::
               "joptsimple;resolution:=optional" ::
               "sun.*;resolution:=optional" :: "*" :: Nil)


    //net.jpountz.lz4:lz4:1.2.0
  )

  lazy val sourceToWrap = List(
    SourceDesc("com.chuusai.shapeless.core", "com.chuusai" %% "shapeless" % "2.0.0" :: Nil),

    SourceDesc("org.scalaz.stream", "org.scalaz.stream" %% "scalaz-stream" % "0.6a" :: Nil, version = Some("0.0.0.6a")),
    SourceDesc("org.scalaz.concurrent", "org.scalaz" %% "scalaz-concurrent" % scalazVersion :: Nil),
    SourceDesc("org.scalaz.core", "org.scalaz" %% "scalaz-core" % scalazVersion :: Nil),
    SourceDesc("org.scalaz.effect", "org.scalaz" %% "scalaz-effect" % scalazVersion :: Nil),
    SourceDesc("org.scalaz.typelevel", "org.scalaz" %% "scalaz-typelevel" % scalazVersion :: Nil),

    SourceDesc("org.typelevel.scodec.bits", "org.typelevel" %% "scodec-bits" % "1.0.4" :: Nil),
    SourceDesc("org.typelevel.scodec.core", "org.typelevel"  %%  "scodec-core" % "1.6.0" :: Nil),
    SourceDesc("org.typelevel.scodec.stream", "org.typelevel"  %%  "scodec-stream" % "0.6.0" :: Nil),

    SourceDesc("com.101tec.zkclient", "com.101tec" % "zkclient" % "0.3" :: Nil),
    SourceDesc("com.datastax.driver.mapping", "com.datastax.cassandra" % "cassandra-driver-mapping" % cassandraVersion :: Nil),
    SourceDesc("com.datastax.driver.core", "com.datastax.cassandra" % "cassandra-driver-core" % cassandraVersion :: Nil)
  )

  lazy val binProjects = bundleToWrap.map {
    bundleSpec => {
      val dep = bundleSpec.deps(0)
      val projectSettings = osgiSettings ++ Seq(
        OsgiKeys.privatePackage := Nil,
        OsgiKeys.exportPackage := bundleSpec.exportPackage, //List("*;version=\"" + dep.revision + "\""),
        OsgiKeys.importPackage := bundleSpec.importPackage,
        OsgiKeys.bundleVersion := dep.revision,
        OsgiKeys.additionalHeaders := bundleSpec.headers,
        OsgiKeys.bundleSymbolicName := bundleSpec.symbolicName,
        OsgiKeys.fragmentHost := bundleSpec.fragmentHost,
        version := dep.revision
      )

      val srcProjectSettings = srcBundlesSettings(bundleSpec.symbolicName, dep.revision, bundleSpec.exportPackage)
      val sourceDeps = asSourceBundles(bundleSpec.deps)
      val binDeps =  bundleSpec.deps.map {
        originalDep => originalDep //intransitive()
      }
      val srcProject = Project(id = dep.name + "-source", base = file(dep.name + "-source")).
            settings(buildSettings ++ srcProjectSettings ++ Seq(libraryDependencies ++= sourceDeps) :_*)
      val binProject = Project(id = dep.name, base = file(dep.name)).
            settings(buildSettings ++ projectSettings ++ Seq(libraryDependencies ++= binDeps) :_*)
      List(binProject, srcProject)
    }
  }.flatten

  lazy val srcProjects = sourceToWrap.map {
    bundleSpec => {
      val dep = bundleSpec.deps(0)
      val depVersion = bundleSpec.version.getOrElse(dep.revision)
      val srcProjectSettings = srcBundlesSettings(bundleSpec.symbolicName, depVersion, bundleSpec.exportPackage)
      val sourceDeps = asSourceBundles(bundleSpec.deps)
      Project(id = dep.name + "-source", base = file(dep.name + "-source")).
          settings(buildSettings ++ srcProjectSettings ++ Seq(libraryDependencies ++= sourceDeps) :_*)
    }
  }

  lazy val bundles = asProjectReferences(binProjects.toSeq ++ srcProjects.toSeq:_*)

  lazy val root = Project(id = "bundlify", base = file("."), aggregate = bundles)

  override def projects = (root :: binProjects) ++ srcProjects

  lazy val buildSettings = Defaults.defaultSettings

  def asProjectReferences(projectsDef: Project*) = {
    projectsDef.map {
      prj => Project.projectToRef(prj)
    }
  }

  def srcBundlesSettings(symbolicName: String, ver: String, exportPackage: Seq[String]) = {
      osgiSettings ++ Seq(
        OsgiKeys.privatePackage := Nil,
        OsgiKeys.exportPackage := exportPackage,
        OsgiKeys.importPackage := Nil,
        OsgiKeys.bundleVersion := ver,
        OsgiKeys.bundleSymbolicName := symbolicName + ".source",
        OsgiKeys.additionalHeaders := Map("Eclipse-SourceBundle" -> (symbolicName + ";version=\"" + ver + "\"")),
        version := ver
    )
  }

  def asSourceBundles(deps: Seq[ModuleID]) = {
    deps.map {
      originalDep => originalDep classifier("sources")
    }
  }

  implicit def moduleToSeqModule(value: ModuleID): Seq[ModuleID] = List(value)
}
