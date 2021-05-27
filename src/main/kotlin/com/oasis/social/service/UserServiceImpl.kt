package com.oasis.social.service

import com.oasis.social.common.GlobalRouter
import com.oasis.social.models.User
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.api.service.ServiceRequest
import io.vertx.ext.web.api.service.ServiceResponse
import io.vertx.ext.web.openapi.RouterBuilder
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.serviceproxy.ServiceBinder
import org.apache.logging.log4j.LogManager

class UserServiceImpl : IUserService, CoroutineVerticle() {
  private val logger = LogManager.getLogger(this::class.java)
  private lateinit var consumer: MessageConsumer<JsonObject>

  override suspend fun start() {
    val serviceBinder = ServiceBinder(vertx)
    val userService = IUserService.create()
    consumer = serviceBinder
      .setAddress(IUserService::class.java.name)
      .register(IUserService::class.java, userService)

    val routerBuilder = RouterBuilder.create(this.vertx, "userapi.json").await()
    // 挂载服务到 event bus
    routerBuilder.mountServicesFromExtensions()
    // 生成路由
    val router = routerBuilder.createRouter()
    // 挂载到全局路由
    GlobalRouter.getRouter().mountSubRouter("/", router)
  }

  override suspend fun stop() {
    consumer.unregister().await()
  }

  override fun getUserList(request: ServiceRequest, resultHandler: Handler<AsyncResult<ServiceResponse>>) {
    TODO("Not yet implemented")
  }

  override fun getUserById(
    userId: String,
    request: ServiceRequest,
    resultHandler: Handler<AsyncResult<ServiceResponse>>
  ) {
    logger.info("userId: $userId")
  }

  override fun createUser(
    user: User?,
    request: ServiceRequest?,
    resultHandler: Handler<AsyncResult<ServiceResponse>>?
  ) {
    logger.info("user: $user")
  }

  override fun updateUser(
    user: User,
    request: ServiceRequest,
    resultHandler: Handler<AsyncResult<ServiceResponse>>
  ) {
    logger.info("user: $user")
  }

  override fun deleteUserById(
    userId: String,
    request: ServiceRequest,
    resultHandler: Handler<AsyncResult<ServiceResponse>>
  ) {
    logger.info("userId: $userId")
  }
}
