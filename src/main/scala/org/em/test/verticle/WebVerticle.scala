package org.em.test.verticle

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.core.MultiMap
import io.vertx.scala.core.http.ServerWebSocket
import io.vertx.scala.ext.web.{Router, RoutingContext}
import io.vertx.scala.ext.web.handler.{BodyHandler, CookieHandler}

import scala.util.{Failure, Success}

class WebVerticle extends ScalaVerticle {


  def routerInit(router: Router) = {

    router.route("/cache").handler(ctx => ctx.vertx().eventBus().sendFuture[Any]("org.em.test.verticle.CacheVerticle", commonParams(ctx)).onComplete {
      case Success(result) => ctx.response().end(result.body().toString)
      case Failure(cause) => fail(ctx, cause)
    })

    router.get("/*").handler(ctx => ctx.response().end("Hello World"))
    router.post("/*").handler(ctx => ctx.vertx().eventBus().sendFuture[String]("org.em.test.verticle.DBVerticle", ctx.getBodyAsJson().get).onComplete {
      case Success(result) => ctx.response().end(result.body())
      case Failure(cause) => fail(ctx, cause)
    })
  }

  private def fail(ctx: RoutingContext, cause: Throwable): Unit = {
    ctx.response().end(s"WTF : ${cause.getCause.getMessage}")
  }

  def commonParams(ctx: RoutingContext): JsonObject = {
    val jsonObject = new JsonObject()
    jsonObject.mergeIn(paramsToJsonObject(ctx.queryParams()))
    jsonObject.mergeIn(ctx.getBodyAsJson().get)
  }

  def paramsToJsonObject(params: MultiMap): JsonObject = {
    val jsonObject = new JsonObject()
    val names = params.names()
    for (name <- names) {
      jsonObject.put(name, params.get(name))
    }
    jsonObject
  }

  override def start(): Unit = {
    println("WebVerticle starting")
    val router = Router.router(vertx)
    router.route().handler(BodyHandler.create())
    //    router.route().handler(CookieHandler.create)
    //    router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)).setCookieHttpOnlyFlag(true).setCookieSecureFlag(true))
    //    router.route().handler(CSRFHandler.create("ojbk"))

    routerInit(router)

    vertx.createHttpServer().requestHandler(router.accept(_)).websocketHandler(websocket => {
      if (!websocket.path().contains("ws")) {
        websocket.reject(400)
      }
      websocket.drainHandler(_ => {
        // todo
        ws(websocket, "", new JsonObject())
      })
      websocket.closeHandler(_ => {
        // todo
        ws(websocket, "", new JsonObject())
      })
    }).listenFuture(8080).onComplete {
      case Success(result) => println(s"Server is now listening!/n result:$result")
      case Failure(cause) => println(s"$cause")
    }

  }

  private def ws(websocket: ServerWebSocket, address: String, message: JsonObject): Unit = {
    vertx.eventBus().sendFuture[JsonObject](address, message).onComplete {
      case Success(result) => websocket.writeTextMessage(result.body().getString(""))
      case Failure(cause) => websocket.writeTextMessage(cause.getMessage)
    }
  }

  override def stop(): Unit = super.stop()
}
