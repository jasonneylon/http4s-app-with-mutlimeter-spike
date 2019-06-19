package com.example.mm

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

object Main extends IOApp {
  def run(args: List[String]) =
    MmServer.stream[IO].compile.drain.as(ExitCode.Success)
}