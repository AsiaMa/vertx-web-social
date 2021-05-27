package com.oasis.social

import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import org.apache.logging.log4j.LogManager


class MainVerticle : CoroutineVerticle() {
  private val logger = LogManager.getLogger(this::class.java)

  override suspend fun start() {
    // 由于使用了Vertx全局Router 所以必须按顺序部署Verticle
    vertx.deployVerticle("kt:com.oasis.social.BaseVerticle").await()
    vertx.deployVerticle("kt:com.oasis.social.service.UserServiceImpl").await()
    vertx.deployVerticle("kt:com.oasis.social.service.ProductServiceImpl").await()
    vertx.deployVerticle("kt:com.oasis.social.WebServerVerticle").await()
    logger.info("All Verticle deployment completed!")
  }
}
