package client

import org.mockserver.client.server.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse

fun main(args: Array<String>) {
  println("preparing mocks")
  println("plain request: " + HttpRequest.request())
  val cli = MockServerClient("localhost", 1090)
  cli.`when`(HttpRequest.request().withMethod("GET"))
    .respond(HttpResponse.response().withBody("oho"))
  cli.`when`(HttpRequest.request().withMethod("POST"))
    .respond(HttpResponse.response().withBody("aha"))
  println("mock set up")
  cli.retrieveActiveExpectations(HttpRequest.request()).forEach {
    println("expectation: " + it)
  }
  println("Number of recorded requests: " + cli.retrieveRecordedRequests(HttpRequest.request()).size)
  /*
  println("messages")
  cli.retrieveLogMessagesArray(HttpRequest.request()).forEach {
    println(it)
  }
  */
}

