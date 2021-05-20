package com.oasis.vertx_web_social

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router

class MainVerticle : AbstractVerticle() {

  override fun start() {
    val router = Router.router(vertx)

    vertx.deployVerticle(JWTVerticle(router))
    vertx.deployVerticle(ProductVerticle(router))
  }
}
