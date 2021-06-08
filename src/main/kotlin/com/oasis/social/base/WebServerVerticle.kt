package com.oasis.social.base

import com.oasis.social.config.appConfig
import com.oasis.social.util.GlobalRouter
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import org.apache.logging.log4j.LogManager

class WebServerVerticle : CoroutineVerticle() {

  private val logger = LogManager.getLogger(WebServerVerticle::class.java)

  override suspend fun start() {
    // 创建httpServer
    val port = appConfig.getJsonObject("http").getInteger("port")
    vertx
      .createHttpServer()
      .requestHandler(GlobalRouter.getRouter())
      .listen(port).onComplete {
        if (it.succeeded()) {
          logger.info("Web server is listening on port $port!")
        } else if (it.cause() != null) {
          logger.error("Web server start failed!", it.cause())
          vertx.close()
        }
      }.await()
    logger.info("WebServerVerticle deployment completed!")
  }
}
