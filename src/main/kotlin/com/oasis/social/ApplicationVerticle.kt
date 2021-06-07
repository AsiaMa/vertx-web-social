package com.oasis.social

import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import org.apache.logging.log4j.LogManager


class ApplicationVerticle : CoroutineVerticle() {
  private val logger = LogManager.getLogger(ApplicationVerticle::class.java)

  override suspend fun start() {
    // 由于使用了Vertx全局Router 所以必须按顺序部署Verticle
    vertx.deployVerticle("kt:com.oasis.social.base.BaseVerticle").await()
    vertx.deployVerticle("kt:com.oasis.social.security.AuthRoleVerticle").await()
    vertx.deployVerticle("kt:com.oasis.social.service.LoginServiceVerticle").await()
    vertx.deployVerticle("kt:com.oasis.social.service.UserServiceVerticle").await()
    vertx.deployVerticle("kt:com.oasis.social.service.ProductServiceVerticle").await()
    vertx.deployVerticle("kt:com.oasis.social.base.WebServerVerticle").await()
    logger.info("All Verticle deployment completed!")
  }
}
