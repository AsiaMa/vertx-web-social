package com.oasis.vertx_web_social

import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await

class MainVerticle : CoroutineVerticle() {

  override suspend fun start() {
    vertx.deployVerticle(JWTVerticle()).await()
    vertx.deployVerticle(ProductVerticle()).await()
  }
}
