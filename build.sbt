name := "Order"

version := "1.6-SNAPSHOT"

ebeanEnabled := true

scalaVersion := "2.10.3"

crossScalaVersions := Seq("2.10.4", "2.11.0-RC4")

libraryDependencies ++= Seq(
  //"com.edulify" %% "geolocation" % "1.2.0",
   javaCore,
  javaJdbc,
  //javaEbean,
  javaJpa,
  cache,
  "mysql" % "mysql-connector-java" % "5.1.3",
  //"ws.securesocial" %% "securesocial" % "2.1.3",
  //"org.mindrot" % "jbcrypt" % "0.3m",
  //"org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final"
  "org.hibernate" % "hibernate-entitymanager" % "4.2.2.Final"
  //"com.typesafe.play" % "sbt-link" % "2.2.2"
  //"com.google.guava" % "guava" % "16.0.1"
  //"org.scala-lang" % "scala-library" % "2.10.0"
)

resolvers ++= Seq(
Resolver.sonatypeRepo("releases"),
"Local Repository" at "file:///home/lmergen/git/playframework/repository/local",
Resolver.url("Edulify Repository", url("http://edulify.github.io/modules/releases/")),
"Scala Repository" at "http://www.scala-lang.org/api/2.10.4/"
)

//resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

//resolvers += "Local Repository" at "file:///home/lmergen/git/playframework/repository/local"

//resolvers += Resolver.sonatypeRepo("releases")

//resolvers += "Edulify Repository" at "https://edulify.github.io/modules/releases/"
//resolvers += Resolver.url("Edulify Repository",url("https://edulify.github.io/modules/releases/"))(Resolver.ivyStylePatterns)

play.Project.playJavaSettings