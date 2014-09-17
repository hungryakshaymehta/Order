name := "Order"

version := "1.6-SNAPSHOT"

ebeanEnabled := true

libraryDependencies ++= Seq(
   javaCore,
  javaJdbc,
  //javaEbean,
  javaJpa,
  cache,
  "mysql" % "mysql-connector-java" % "5.1.3",
  "ws.securesocial" %% "securesocial" % "2.1.3",
  "org.mindrot" % "jbcrypt" % "0.3m",
  //"org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final"
  "org.hibernate" % "hibernate-entitymanager" % "4.2.2.Final"
)

resolvers += Resolver.sonatypeRepo("releases")

play.Project.playJavaSettings