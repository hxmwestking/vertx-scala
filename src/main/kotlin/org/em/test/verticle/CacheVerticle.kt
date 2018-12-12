package org.em.test.verticle

import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject
import java.util.*

class CacheVerticle : AbstractVerticle() {

    private val map = HashMap<String, Any>()

    override fun start() {
        vertx.eventBus().consumer<JsonObject>(this::class.java.name) {
            val body = it.body()
            val type = body.getString("type")
            val key = body.getString("key")
            when (type) {
                "set" -> {
                    map[key] = body.getValue("value")
                    it.reply("ok")
                }
                "get" -> it.reply(map[key])
            }
        }
        println("CacheVerticle over")
    }

    override fun stop() {
        super.stop()
    }
}