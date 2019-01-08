package org.em.test.verticle;

import io.reactiverse.pgclient.*;
import io.reactiverse.pgclient.PgRowSet;
import io.vertx.core.AbstractVerticle;

/**
 * @author ximan.huang
 * @description todo
 * @date 2019/1/8 17:01
 */
public class PGVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        dbInit();
    }

    private void dbInit() {
        PgPoolOptions options = new PgPoolOptions()
                .setPort(5432)
                .setHost("localhost")
                .setDatabase("test")
                .setUser("user")
                .setPassword("secret")
                .setMaxSize(5);

        // Create the client pool
        PgPool client = PgClient.pool(vertx, options);

        // A simple query
        client.query("SELECT * FROM users WHERE id='julien'", ar -> {
            if (ar.succeeded()) {
                PgRowSet result = ar.result();
                System.out.println("Got " + result.size() + " rows ");
            } else {
                System.out.println("Failure: " + ar.cause().getMessage());
            }

            // Now close the pool
            client.close();
        });
    }

    private void query(){
        // Create the pool from the environment variables
        PgPool pool = PgClient.pool();

        // Create the connection from the environment variables
        PgClient.connect(vertx, res -> {
            // Handling your connection
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
