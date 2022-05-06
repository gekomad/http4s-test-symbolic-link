val Http4sVersion = "1.0.0-M32"
val scalatestVersion = "3.3.0-SNAP3"

version := "0.0.1-SNAPSHOT"
scalaVersion := "2.13.8"

libraryDependencies += "org.http4s" %% "http4s-blaze-server" % Http4sVersion
libraryDependencies += "org.http4s" %% "http4s-dsl" % Http4sVersion
libraryDependencies += "org.http4s" %% "http4s-blaze-client" % Http4sVersion
libraryDependencies += "org.scalatest" %% "scalatest" % scalatestVersion % Test
