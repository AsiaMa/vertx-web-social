package com.oasis.social.service

import com.oasis.social.models.User
import com.oasis.social.persistence.IUserPersistence
import com.oasis.social.util.GlobalRouter
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.api.service.ServiceRequest
import io.vertx.ext.web.api.service.ServiceResponse
import io.vertx.ext.web.openapi.RouterBuilder
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.serviceproxy.ServiceBinder
import org.apache.logging.log4j.LogManager

class UserServiceVerticle() : IUserService, CoroutineVerticle() {
  private val logger = LogManager.getLogger(UserServiceVerticle::class.java)
  private lateinit var consumer: MessageConsumer<JsonObject>

  private var userPersistence: IUserPersistence? = null

  constructor(userPersistence: IUserPersistence) : this() {
    this.userPersistence = userPersistence
  }

  override suspend fun start() {
    val userPersistence = IUserPersistence.create()
    val userService = IUserService.create(userPersistence)

    val serviceBinder = ServiceBinder(vertx)
    consumer = serviceBinder
      .setAddress(IUserService::class.java.name)
      .register(IUserService::class.java, userService)

    val routerBuilder = RouterBuilder.create(this.vertx, "api/userapi.yaml").await()
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
    userPersistence!!.findUsers().onSuccess { userList ->
      val jsonArray = JsonArray()
      userList.forEach { user ->
        jsonArray.add(user.toJson())
      }
      resultHandler.handle(Future.succeededFuture(ServiceResponse.completedWithJson(jsonArray)))
    }.onFailure {
      logger.error("getUserList => ${it.printStackTrace()}")
    }
  }

  override fun getUserById(
    userId: String,
    request: ServiceRequest,
    resultHandler: Handler<AsyncResult<ServiceResponse>>
  ) {
    logger.debug("getUserById => userId: $userId")
    userPersistence!!.findUserById(userId).onSuccess { user ->
      if (user == null) {
        resultHandler.handle(Future.succeededFuture(ServiceResponse.completedWithJson(JsonObject())))
      } else {
        resultHandler.handle(Future.succeededFuture(ServiceResponse.completedWithJson(user.toJson())))
      }
    }.onFailure {
      logger.error("getUserById => create user failed, error message: ${it.message}")
    }
  }

  override fun createUser(
    body: User,
    request: ServiceRequest,
    resultHandler: Handler<AsyncResult<ServiceResponse>>
  ) {
    logger.debug("createUser => body: $body")
    userPersistence!!.addUser(body).onSuccess {
      resultHandler.handle(
        Future.succeededFuture(
          ServiceResponse.completedWithJson(
            JsonObject().put("code", 200).put("msg", "success")
          )
        )
      )
    }.onFailure {
      logger.error("createUser => create user failed, error message: ${it.message}")
    }
  }

  override fun updateUser(
    userId: String,
    body: User,
    request: ServiceRequest,
    resultHandler: Handler<AsyncResult<ServiceResponse>>
  ) {
    logger.debug("updateUser => userId: $userId, body: $body")
    userPersistence!!.updateUser(userId, body).onSuccess {
      resultHandler.handle(
        Future.succeededFuture(
          ServiceResponse.completedWithJson(
            JsonObject().put("code", 200).put("msg", "success")
          )
        )
      )
    }.onFailure {
      logger.error("updateUser => create user failed, error message: ${it.message}")
    }
  }

  override fun deleteUserById(
    userId: String,
    request: ServiceRequest,
    resultHandler: Handler<AsyncResult<ServiceResponse>>
  ) {
    logger.debug("deleteUserById => userId: $userId")
    userPersistence!!.deleteUserById(userId).onSuccess {
      resultHandler.handle(
        Future.succeededFuture(
          ServiceResponse.completedWithJson(
            JsonObject().put("code", 200).put("msg", "success")
          )
        )
      )
    }.onFailure {
      logger.error("deleteUserById => create user failed, error message: ${it.message}")
    }
  }
}
