package client

import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity

fun main(args: Array<String>) {
  SimulateClient.runTests()
}

object SimulateClient {
  fun get1() {
    val resp = ClientBuilder.newClient()
      .target("http://mirrorhost.com/anypath")
      .request()
      //.post(Entity.text("asdf"));
      .get()
    println(resp.readEntity(String::class.java))
  }

  fun post1() {
    val resp = ClientBuilder.newClient()
      .target("http://asdfasdfasdfa.uiopuoirte/anypath")
      .request()
      .post(Entity.text("asdf"));
    println(resp.readEntity(String::class.java))
  }

  fun runTests() {
    get1()
    post1()
  }
}