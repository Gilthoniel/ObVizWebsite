name := """obviz-app"""

version := "0.1"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.webjars" % "jquery" % "2.1.4",
  "com.google.code.gson" % "gson" % "2.3.1"
)

// LESS files
includeFilter in (Assets, LessKeys.less) := "design.less"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
