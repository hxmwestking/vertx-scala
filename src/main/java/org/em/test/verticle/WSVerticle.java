package org.em.test.verticle;

import com.google.common.collect.HashBiMap;
import io.vertx.core.AbstractVerticle;

import java.util.Map;


public class WSVerticle extends AbstractVerticle {

    private Map<String,String> map = HashBiMap.create();

    @Override
    public void start() throws Exception {
        // todo
        super.start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
