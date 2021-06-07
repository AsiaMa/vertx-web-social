package com.oasis.social.base

import com.oasis.social.util.GlobalRouter
import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.config.configStoreOptionsOf
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import org.apache.logging.log4j.LogManager

class WebServerVerticle : CoroutineVerticle() {

  private val logger = LogManager.getLogger(WebServerVerticle::class.java)

  override suspend fun start() {
    // 创建httpServer
    // 使用全局Router Router里的所有handler都会在当前线程执行
    val store =
      configStoreOptionsOf(type = "file", format = "hocon", config = JsonObject().put("path", "application.conf"))
    val configRetriever = ConfigRetriever.create(vertx, ConfigRetrieverOptions().addStore(store))
    val config = configRetriever.config.await()

    val port = config.getJsonObject("http").getInteger("port")
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
