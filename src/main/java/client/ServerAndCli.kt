package client

import org.mockserver.client.server.MockServerClient
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response

fun main(args: Array<String>) {
  println("starting the server")
  val cliSer = ClientAndServer.startClientAndServer(1090)
  println("is running? ${cliSer.isRunning()}")
  cliSer.`when`(request().withPath("/mirror")).callback { req ->
    //espeak("yeah")
    response("callback for " + req::class)
    req.headers.entries.forEach { println(it) }
    response("ok")
  }
  cliSer.`when`(request().withHeader(Header.header("Host", "mirrorhost.com")))
    .respond(response("mirror host"))
  cliSer.`when`(request(null)).callback { req ->
    espeak("no expectation")
    req.headers.entries.forEach { println("header: $it") }
    response("ok")
  }
  SimulateClient.runTests()
  //cliSer.stop()
  System.exit(0)
}

