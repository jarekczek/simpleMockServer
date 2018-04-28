package client

import org.mockserver.client.server.MockServerClient
import org.mockserver.model.HttpRequest

fun main(args: Array<String>) {
  println("let's reset")
  val cli = MockServerClient("localhost", 1090)
  cli.reset()
  cli.clear(HttpRequest.request().withMethod("GET"))
  cli.clear(null)
  println("reset done")
}

