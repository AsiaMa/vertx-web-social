package com.oasis.social

import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import org.slf4j.LoggerFactory

class MainVerticle : CoroutineVerticle() {
  private val logger = LoggerFactory.getLogger(this::class.java)

  override suspend fun start() {
    // 由于使用了Vertx全局Router 所以必须按顺序部署Verticle
    vertx.deployVerticle("kt:com.oasis.social.BaseVerticle").await()
    vertx.deployVerticle("kt:com.oasis.social.WebServerVerticle").await()
    logger.info("All Verticle deployment completed!")
  }
}
