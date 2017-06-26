lazy val `cluster` =
  project.in(file("."))
  .aggregate(
    `api`,
    `cluster-seeds`,
    `cluster-listener`,
    `cluster-agent`,
    `cluster-multi-agent`,
    `cluster-client`
  )
  .settings(commonSettings: _*)
  .enablePlugins(GitVersioning)

lazy val `api` =
  project.in(file("api"))
  .settings(commonSettings: _*)
  .settings(
    name := "api",
    libraryDependencies ++= commonLibraries
  )
  .enablePlugins(GitVersioning)

lazy val `cluster-seeds` =
  project.in(file("cluster-seeds"))
  .dependsOn(`api`)
  .settings(commonSettings: _*)
  .settings(
    name := "cluster-seeds",
    libraryDependencies ++= commonLibraries
  )
  .enablePlugins(JavaServerAppPackaging, OpenShiftPlugin, GitVersioning)
  .settings(dockerSettings: _*)

lazy val `cluster-listener` =
  project.in(file("cluster-listener"))
  .dependsOn(`api`)
  .settings(commonSettings: _*)
  .settings(
    name := "cluster-listener",
    libraryDependencies ++= commonLibraries
  )
  .enablePlugins(JavaServerAppPackaging, OpenShiftPlugin, GitVersioning)
  .settings(dockerSettings: _*)

lazy val `cluster-agent` =
  project.in(file("cluster-agent"))
  .dependsOn(`api`)
  .settings(commonSettings: _*)
  .settings(
    name := "cluster-agent",
    libraryDependencies ++= commonLibraries
  )
  .enablePlugins(JavaServerAppPackaging, OpenShiftPlugin, GitVersioning)
  .settings(dockerSettings: _*)

lazy val `cluster-multi-agent` =
  project.in(file("cluster-multi-agent"))
  .dependsOn(`api`)
  .settings(commonSettings: _*)
  .settings(
    name := "cluster-multi-agent",
    libraryDependencies ++= commonLibraries
  )
  .enablePlugins(JavaServerAppPackaging, OpenShiftPlugin, GitVersioning)
  .settings(dockerSettings: _*)

lazy val `cluster-client` =
  project.in(file("cluster-client"))
  .dependsOn(`api`)
  .settings(commonSettings: _*)
  .settings(
    name := "cluster-client",
    libraryDependencies ++= commonLibraries
  )
  .enablePlugins(JavaServerAppPackaging, OpenShiftPlugin, GitVersioning)
  .settings(dockerSettings: _*)

lazy val commonSettings = Seq(
  organization := "com.ctc.example",
  git.useGitDescribe := true,

  scalaVersion := "2.12.2",
  parallelExecution := false,

  scalacOptions ++= Seq(
    "-encoding", "UTF-8",

    "-feature",
    "-unchecked",
    "-deprecation",

    "-language:postfixOps",
    "-language:implicitConversions",

    "-Ywarn-unused-import",
    "-Xfatal-warnings",
    "-Xlint:_"
  ),
  concurrentRestrictions in Global += Tags.limit(Tags.Test, 1)
)

lazy val commonLibraries = {
  val akkaVersion = "2.5.2"
  val akkaHttpVersion = "10.0.7"
  val scalatestVersion = "3.0.3"

  Seq(
    "com.iheart" %% "ficus" % "1.4.0",
    "io.spray" %% "spray-json" % "1.3.3",
    "org.julienrf" %% "play-json-derived-codecs" % "4.0.0-RC1",

    "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
    "com.lightbend.akka" %% "akka-management-cluster-http" % "0.3",

    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream-kafka" % "0.16",
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,

    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",

    "org.scalactic" %% "scalactic" % scalatestVersion % Test,
    "org.scalatest" %% "scalatest" % scalatestVersion % Test,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
    "net.manub" %% "scalatest-embedded-kafka" % "0.13.1" % Test
  )
}

lazy val dockerSettings = {
  Seq(
    dockerExposedPorts := Seq(9000),
    dockerRepository := Some("example-akka-cluster"),
    version in Docker := name.value + "-" + version.value.replaceFirst("""-SNAPSHOT""", ""),
    dockerUpdateLatest := true
  )
}
