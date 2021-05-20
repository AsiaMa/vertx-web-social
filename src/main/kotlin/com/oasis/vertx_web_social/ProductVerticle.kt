package com.oasis.vertx_web_social

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router

class ProductVerticle : AbstractVerticle() {
  override fun start() {
    val router = Router.router(vertx)
    router.route("/api/product").handler { ctx ->
      ctx.response().putHeader("content-type", "application/json")
      ctx.json("Big computer")
    }

    vertx.createHttpServer().requestHandler(router).listen(8888) { ar ->
      if (ar.succeeded()) {
        println("success ${ar.result()}")
      } else {
        println(ar.cause())
      }
    }
  }
}
