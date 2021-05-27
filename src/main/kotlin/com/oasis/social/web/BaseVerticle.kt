package com.oasis.social.web

import com.oasis.social.common.GlobalRouter
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.CorsHandler
import io.vertx.ext.web.handler.LoggerFormat
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.slf4j.LoggerFactory

class BaseVerticle : CoroutineVerticle() {

  private val logger = LoggerFactory.getLogger(this::class.java)

  private val allHeader = "accept, accept-encoding, authorization, content-type, dnt, origin, user-agent, " +
    "x-csrftoken, x-requested-with, *"

  override suspend fun start() {
    // 注意!! router的handler会在WebServerVerticle的EventLoop线程执行
    // 不要在handler里使用非线程安全内容 以下handler都是线程安全的
    val router = GlobalRouter.getRouter()
    // 请求日志输出handler
    router.route().handler(LoggerHandler.create(LoggerFormat.SHORT))
    // 跨域请求处理handler
    router.route().handler(
      CorsHandler.create(".*")
        .maxAgeSeconds(3600)
        .allowCredentials(true)
        .allowedHeader(allHeader)
        .allowedMethods(HttpMethod.values().toSet())
    )
    // 请求Body处理handler
    router.route().handler(BodyHandler.create().setBodyLimit(100 * 1048576L)) //1MB = 1048576L
    // 全局错误处理 不用把错误信息暴露给用户端
    router.errorHandler(500) {
      logger.error("An exception occurred on the server", it.failure())
      it.fail(500)
      it.end("An exception occurred on the server")
    }
    logger.info("BaseVerticle deployment completed!")
  }
}

