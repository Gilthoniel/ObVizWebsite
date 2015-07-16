name := """obviz-app"""

version := "0.1"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.webjars" % "jquery" % "2.1.4",
  "com.google.code.gson" % "gson" % "2.3.1",
  "com.feth" %% "play-authenticate" % "0.7.0"
)

// LESS files
includeFilter in (Assets, LessKeys.less) := "design.less" | "admin.less"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
