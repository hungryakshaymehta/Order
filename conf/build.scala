import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "TestingJPA"
  val appVersion      = "1.0"

  val appDependencies = Seq(
      javaCore,
      javaJdbc,
      javaJpa,
      "org.hibernate" % "hibernate-entitymanager" % "4.2.2.Final"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // disable Ebean ORM
    ebeanEnabled := false        
  )

}