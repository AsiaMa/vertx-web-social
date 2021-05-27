package com.oasis.social

import com.oasis.social.common.GlobalRouter
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import org.apache.logging.log4j.LogManager

class WebServerVerticle : CoroutineVerticle() {

  private val logger = LogManager.getLogger(this::class.java)

  override suspend fun start() {
    // 创建httpServer
    // 使用全局Router Router里的所有handler都会在当前线程执行
    val port = 8888
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
