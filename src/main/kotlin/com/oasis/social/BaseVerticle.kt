package com.oasis.social

import com.oasis.social.util.GlobalRouter
import com.oasis.social.util.JWTUtils
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.handler.*
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.apache.logging.log4j.LogManager

class BaseVerticle : CoroutineVerticle() {

  private val logger = LogManager.getLogger(BaseVerticle::class.java)

  private val allHeader =
    "accept, accept-encoding, authorization, content-type, dnt, origin, user-agent, x-csrftoken, x-requested-with, *"

  override suspend fun start() {

    val router = GlobalRouter.getRouter()
    // 请求日志输出handler
    router.route().handler(LoggerHandler.create(LoggerFormat.SHORT))
    // 跨域请求处理handler
    router.route().handler(
      CorsHandler.create("*")
        .maxAgeSeconds(3600)
        .allowCredentials(true)
        .allowedHeader(allHeader)
        .allowedMethods(HttpMethod.values().toSet())
    )
    // 请求Body处理handler
    router.route().handler(BodyHandler.create().setBodyLimit(100 * 1048576L)) //1MB = 1048576L
    // 全局错误处理
    router.route().failureHandler {
      it.failure().printStackTrace()
      logger.error("An exception occurred on the server", it.failure())
      it.end(it.failure().message)
    }

    //对/api开头的所有接口进行用户认证
    router.route("/api/*")
      .handler(JWTAuthHandler.create(JWTUtils.getJWTAuth()))

    logger.info("BaseVerticle deployment completed!")
  }
}

