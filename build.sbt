name := "webAPI"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  filters,
  "com.adrianhurt" %% "play-bootstrap" % "1.0-P25-B4-SNAPSHOT",
  "org.apache.avalon.framework" % "avalon-framework-api" % "4.2.0" from "http://repo1.maven.org/maven2/avalon-framework/avalon-framework-api/4.2.0/avalon-framework-api-4.2.0.jar",
  "org.apache.avalon.framework" % "avalon-framework-impl" % "4.2.0" from "http://repo1.maven.org/maven2/avalon-framework/avalon-framework-impl/4.2.0/avalon-framework-impl-4.2.0.jar",
  "org.apache.xmlgraphics" % "fop" % "1.1"
)

play.Project.playJavaSettings
