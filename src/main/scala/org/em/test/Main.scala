package org.em.test

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.{DeploymentOptions, Vertx}
import org.em.test.verticle.WebVerticle

object Main {
  def main(args: Array[String]): Unit = {
    val vertx = Vertx.vertx()
    vertx.deployVerticle(ScalaVerticle.nameForVerticle[WebVerticle],DeploymentOptions().setInstances(Runtime.getRuntime.availableProcessors() * 2))
    vertx.deployVerticle("org.em.test.verticle.DBVerticle")
    vertx.deployVerticle("org.em.test.verticle.CacheVerticle")
  }
}