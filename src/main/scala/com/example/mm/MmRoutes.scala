package com.example.mm

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.core.instrument.MeterRegistry

object MmRoutes {

  def jokeRoutes[F[_]: Sync](J: Jokes[F], registry: MeterRegistry): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "joke" => {
        for {
          joke <- J.get
          resp <- Ok(joke)
        } yield resp
      }
    }
  }

  def helloWorldRoutes[F[_]: Sync](H: HelloWorld[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(HelloWorld.Name(name))
          resp <- Ok(greeting)
        } yield resp
    }
  }


  def metricsRoutes[F[_]: Sync](registry: PrometheusMeterRegistry): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "metrics" => {
        val m = registry.scrape()
        for {
          resp <- Ok(m)
        } yield resp
        }
    }
  }
}
