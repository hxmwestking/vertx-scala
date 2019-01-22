package org.em.test.verticle;


import com.google.common.collect.Lists;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.BulkOperation;
import io.vertx.ext.mongo.MongoClient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description mongoDB
 * @author ximan.huang
 * @date 2018/12/24 17:20
 */
public class DBVerticle extends AbstractVerticle {

    private MongoClient mongoClient;
    private Map<String, Object> map = new HashMap<>();
    private static final String KEY = "key";

    @Override
    public void start() throws Exception {
        dbInit();
        mapInit();
        vertx.eventBus().<JsonObject>consumer(this.getClass().getName(), msg -> {
            JsonObject body = msg.body();
            if (body.isEmpty() || !body.containsKey(KEY)) {
                msg.reply("No such person");
                return;
            }
            String key = body.getString(KEY);
            msg.reply(key + " : " + map.get(key));
        });
        System.out.println("DBVerticle over");

    }

    private void mapInit() {
        map.put("Yuri", 29);
        map.put("JiYeon", 25);
        map.put("TaeYeon", 30);
    }

    private void dbInit(){
        mongoClient = MongoClient.createShared(vertx, new JsonObject()
                .put("db_name", "test"),"test");
        mongoClient.find("wives",new JsonObject().put("type","init"), dbInitRes());
    }

    @NotNull
    private Handler<AsyncResult<List<JsonObject>>> dbInitRes() {
        return res->{
           if (res.succeeded()){
               if (res.result().size()>0){
                   System.out.println("data already exits");
               }else {
                   initData();
               }
           }else {
               res.cause().printStackTrace();
           }
        };
    }

    private void initData() {
        ArrayList<BulkOperation> list = Lists.newArrayList();
        list.add(BulkOperation.createInsert(new JsonObject().put("type","init").put("name","Yuri").put("age",29)));
        list.add(BulkOperation.createInsert(new JsonObject().put("type","init").put("name","JiYeon").put("age",25)));
        list.add(BulkOperation.createInsert(new JsonObject().put("type","init").put("name","TaeYeon").put("age",30)));
        mongoClient.bulkWrite("wives",list,ar->{
            if (ar.succeeded()){
                System.out.println("dbInit success");
            }else {
                ar.cause().printStackTrace();
            }
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
