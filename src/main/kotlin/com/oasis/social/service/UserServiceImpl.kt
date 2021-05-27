package com.oasis.social.service

import com.oasis.social.common.GlobalRouter
import com.oasis.social.models.User
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.ext.web.api.service.ServiceRequest
import io.vertx.ext.web.api.service.ServiceResponse
import io.vertx.ext.web.openapi.RouterBuilder
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.serviceproxy.ServiceBinder
import org.slf4j.LoggerFactory

class UserServiceImpl : IUserService, CoroutineVerticle() {
  private val logger = LoggerFactory.getLogger(this::class.java)

  override suspend fun start() {
    val serviceBinder = ServiceBinder(vertx)
    val userService = IUserService.create()
    val consumer = serviceBinder
      .setAddress(IUserService::class.java.name)
      .register(IUserService::class.java, userService)

    val routerBuilder = RouterBuilder.create(this.vertx, "userapi.json").await()
    // 挂载服务到 event bus
    routerBuilder.mountServicesFromExtensions()
    // 生成路由
    val router = routerBuilder.createRouter()
    // 挂载到全局路由
    GlobalRouter.getRouter().mountSubRouter("", router)

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
