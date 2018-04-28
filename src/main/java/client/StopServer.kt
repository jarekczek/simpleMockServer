package client

import org.mockserver.client.server.MockServerClient

fun main(args: Array<String>) {
  println("let's stop it")
  val cli = MockServerClient("localhost", 1080)
  cli.stop()
  println("stopped")
}

