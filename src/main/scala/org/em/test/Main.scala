package org.em.test

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.{DeploymentOptions, Vertx}
import org.em.test.verticle.{WSVerticle2, WebVerticle}

object Main {
  def main(args: Array[String]): Unit = {

    // 关闭DNS解析，默认走google，会被墙
    System.setProperty("vertx.disableDnsResolver","true")

    val vertx = Vertx.vertx()
    vertx.deployVerticle(ScalaVerticle.nameForVerticle[WebVerticle],DeploymentOptions().setInstances(Runtime.getRuntime.availableProcessors() * 2))
    vertx.deployVerticle("org.em.test.verticle.DBVerticle")
    vertx.deployVerticle("org.em.test.verticle.CacheVerticle")
//    vertx.deployVerticle("org.em.test.verticle.WSVerticle")
    vertx.deployVerticle(ScalaVerticle.nameForVerticle[WSVerticle2])
  }
}
