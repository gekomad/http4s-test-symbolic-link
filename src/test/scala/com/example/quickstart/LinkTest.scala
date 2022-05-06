package com.example.quickstart

import cats.data.Kleisli
import cats.effect.unsafe.implicits.global
import cats.effect.{FiberIO, IO}
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.client.{Client, JavaNetClientBuilder}
import org.http4s.dsl.io._
import org.http4s.{HttpRoutes, Request, Response, StaticFile}
import org.scalatest.funsuite.AnyFunSuite

class LinkTest extends AnyFunSuite {

  def start(httpApp: Kleisli[IO, Request[IO], Response[IO]], host: String, port: Int) = {
    import cats.effect._
    BlazeServerBuilder[IO].bindHttp(port, host).withHttpApp(httpApp)
  }

  val routes = HttpRoutes.of[IO] {
    case request@GET -> Root / "file" =>
      StaticFile.fromPath(fs2.io.file.Path("/tmp/myfile.txt"), Some(request)).getOrElseF(NotFound())
    case request@GET -> Root / "link" =>
      StaticFile.fromPath(fs2.io.file.Path("/tmp/link.txt"), Some(request)).getOrElseF(NotFound())
  }

  def fiber(httpApp: Kleisli[IO, Request[IO], Response[IO]], host: String, port: Int): FiberIO[Nothing] =
    start(httpApp, host, port).resource.use(_ => IO.never).start.unsafeRunSync()

  test("test file and link") {
    println()
    println("before create a file and a symbolik link:")
    println("""yes "Some text" | head -n 1000 > /tmp/myfile.txt""")
    println("""ln -s /tmp/myfile.txt /tmp/link.txt""")
    println()
    fiber(routes.orNotFound, "0.0.0.0", 9000)
    val httpClient: Client[IO] = JavaNetClientBuilder[IO].create
    print("TEST FILE....")
    assert(10000 == httpClient.expect[String]("http://localhost:9000/file").unsafeRunSync().length)
    println("OK")
    print("TEST LINK....")
    assert(10000 == httpClient.expect[String]("http://localhost:9000/link").unsafeRunSync().length)
    println("OK")
  }

}