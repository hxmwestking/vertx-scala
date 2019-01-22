package org.em.test.verticle;

import com.google.common.collect.Maps;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

import java.util.Map;

/**
 * @description todo
 * @author ximan.huang
 * @date 2018/12/24 17:20
 */
public class WSVerticle extends AbstractVerticle {

    private Map<String, JsonObject> map = Maps.newHashMap();
    private static final String WS_PATH = "ws";

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().websocketHandler(websocket->{
           if (!websocket.path().contains(WS_PATH)){
               websocket.reject(400);
           }
           websocket.textMessageHandler(msg->{
               System.out.println(msg);
               map.putIfAbsent(websocket.textHandlerID(),new JsonObject());
               websocket.writeTextMessage("ok");
           });
        });
        System.out.println("WSVerticle start : "+this);
        map.forEach((key,value)->{

        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
