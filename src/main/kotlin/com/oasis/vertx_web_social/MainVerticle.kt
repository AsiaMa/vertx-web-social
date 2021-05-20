package com.oasis.vertx_web_social

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.ext.web.Router

class MainVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>) {
    val router = Router.router(vertx)
    router.route("/hello").handler { ctx ->
      ctx.response().putHeader("context-type", "text/plain")
      ctx.end("hello world")
    }
    vertx.deployVerticle(ProductVerticle())
  }
}
