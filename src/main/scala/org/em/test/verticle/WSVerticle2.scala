package org.em.test.verticle

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.core.eventbus.Message


class WSVerticle2 extends ScalaVerticle {

  var map:Map[String,JsonObject] = Map()

  override def start(): Unit = {
    initWS

    System.out.println("WSVerticle start")
  }

  private def sendGroup(address: String, msg: String) = {
    println(s"msg: $msg")
    if (!map.contains(address)) {
      map += (address -> new JsonObject())
    }
    for ((key, _) <- map) {
      vertx.eventBus().send(key, s"send msg : $msg")
    }
  }

  def remove(address: String) = {
    println(s"address: $address")
    map -= address
  }

  private def initWS = {
    vertx.eventBus().consumer[JsonObject](ScalaVerticle.nameForVerticle[WSVerticle2], (message: Message[JsonObject]) => {
      val jsonObject = message.body()
      println(s"I have received a message: $jsonObject")
      val wsType = jsonObject.getInteger("type").intValue()
      wsType match {
        case 0 => sendGroup(jsonObject.getString("address"),jsonObject.getString("data"))
        case 1 => remove(jsonObject.getString("address"))
      }
      message.reply(new JsonObject().put("sendType",1))
    })
  }

  override def stop(): Unit = super.stop()
}
