package com.oasis.social.service

import com.oasis.social.util.GlobalRouter
import com.oasis.social.util.JWTUtils
import io.netty.util.internal.StringUtil
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.JWTOptions
import io.vertx.ext.web.api.service.ServiceRequest
import io.vertx.ext.web.api.service.ServiceResponse
import io.vertx.ext.web.openapi.RouterBuilder
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.serviceproxy.ServiceBinder
import org.apache.logging.log4j.LogManager

class LoginServiceVerticle : ILoginService, CoroutineVerticle() {
  private val logger = LogManager.getLogger(LoginServiceVerticle::class.java)
  private lateinit var consumer: MessageConsumer<JsonObject>

  override suspend fun start() {
    val serviceBinder = ServiceBinder(this.vertx)
    val authService = ILoginService.create()
    // 订阅消息 eventBus.consumer(address, handler)
    consumer = serviceBinder.setAddress(ILoginService::class.java.name)
      .register(ILoginService::class.java, authService)
    // 解析openapi文件
    val routerBinder = RouterBuilder.create(this.vertx, "api/authapi.yaml").await()
    // 挂载服务到event bus
    routerBinder.mountServicesFromExtensions()
    // 生成路由对应的handler
    val router = routerBinder.createRouter()
    // 挂载到全局路由
    GlobalRouter.getRouter().mountSubRouter("/", router)
  }

  override fun generateToken(
    body: JsonObject,
    request: ServiceRequest,
    resultHandler: Handler<AsyncResult<ServiceResponse>>
  ) {
    val authorities = mutableListOf<String>()
    logger.debug("generateToken => body: $body")

    val authority = body.getString("authority")
    if (!StringUtil.isNullOrEmpty(authority)) {
      for (item in authority.split(",")) {
        authorities.add(item.trim())
      }
    }
    // 生成 token
    val payload = JsonObject().put("userId", 123456).put("userName", "Bob")
    val jwtOptions = JWTOptions().setExpiresInMinutes(10).setPermissions(authorities)
    resultHandler.handle(
      Future.succeededFuture(
        ServiceResponse.completedWithJson(
          JsonObject().put("token", JWTUtils.getJWTAuth().generateToken(payload, jwtOptions))
        )
      )
    )
  }
}
