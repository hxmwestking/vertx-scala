package org.em.test.verticle;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class DBVerticle extends AbstractVerticle {

    private Map<String, Object> map = new HashMap<>();

    @Override
    public void start() throws Exception {
        mapInit();
        vertx.eventBus().<JsonObject>consumer(this.getClass().getName(), msg -> {
            JsonObject body = msg.body();
            if (body.isEmpty() || !body.containsKey("key")) {
                msg.reply("No such person");
                return;
            }
            String key = body.getString("key");
            msg.reply(key + " : " + map.get(key));
        });
        System.out.println("DBVerticle over");

    }

    private void mapInit() {
        map.put("Yuri", 29);
        map.put("JiYeon", 25);
        map.put("TaeYeon", 30);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
