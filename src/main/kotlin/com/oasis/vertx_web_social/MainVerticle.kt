package com.oasis.vertx_web_social

import io.vertx.core.AbstractVerticle

class MainVerticle : AbstractVerticle() {

  override fun start() {
    vertx.deployVerticle(JWTVerticle())
    vertx.deployVerticle(ProductVerticle())
  }
}
