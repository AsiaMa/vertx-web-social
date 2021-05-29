package com.oasis.social.service

import com.oasis.social.models.Product
import com.oasis.social.persistence.IProductPersistence
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

class ProductServiceImpl() : IProductService, CoroutineVerticle() {
  private val logger = LogManager.getLogger(this::class.java)
  private lateinit var consumer: MessageConsumer<JsonObject>
  private var productPersistence: IProductPersistence? = null

  constructor(productPersistence: IProductPersistence) : this() {
    this.productPersistence = productPersistence
  }

  override suspend fun start() {
    val serviceBinder = ServiceBinder(this.vertx)
    val productPersistence = IProductPersistence.create()
    val productService = IProductService.create(productPersistence)
    //订阅eventbus某个地址的消息
    consumer = serviceBinder
      .setAddress(IProductService::class.java.name)
      .register(IProductService::class.java, productService)
    // 解析productapi.json文件，将结果存入一个LinkedHashMap中，key是operationId，value是OperationImpl
    // OperationImpl主要有operationId,method,path,pathModel,operationModel
    val routerBinder = RouterBuilder.create(this.vertx, "productapi.json").await()
    // 挂载服务到 event bus(其实是给OperationImpl的ebServiceAddress、ebServiceMethodName字段赋值，生成路由的时候需要使用到)
    routerBinder.mountServicesFromExtensions()
    // 生成路由与对应handler(RouteToEBServiceHandlerImpl#handler)
    val router = routerBinder.createRouter()
    // 挂载到全局路由
    GlobalRouter.getRouter().mountSubRouter("/", router)
  }

  override suspend fun stop() {
    consumer.unregister().await()
  }

  override fun getProductList(request: ServiceRequest, resultHandler: Handler<AsyncResult<ServiceResponse>>) {
    productPersistence!!.findProducts().onSuccess { productCollection ->
      resultHandler.handle(Future.succeededFuture(ServiceResponse.completedWithJson(JsonArray(productCollection.toList()))))
    }.onFailure {
      logger.error("getProductList => ${it.printStackTrace()}")
    }
  }

  override fun getProductById(
    productId: String,
    request: ServiceRequest,
    resultHandler: Handler<AsyncResult<ServiceResponse>>
  ) {
    logger.debug("getProductById => productId: $productId")
    productPersistence!!.findProductById(productId).onSuccess { product ->
      if (product == null) {
        resultHandler.handle(Future.succeededFuture(ServiceResponse.completedWithJson(JsonObject())))
      } else {
        resultHandler.handle(Future.succeededFuture(ServiceResponse.completedWithJson(product.toJson())))
      }
    }.onFailure {
      logger.error("createUser => create user failed, error message: ${it.message}")
    }
  }

  override fun createProduct(
    body: Product,
    request: ServiceRequest,
    resultHandler: Handler<AsyncResult<ServiceResponse>>
  ) {
    logger.info("product: $body")
  }

  override fun updateProduct(
    body: Product,
    request: ServiceRequest,
    resultHandler: Handler<AsyncResult<ServiceResponse>>
  ) {
    logger.info("updateProduct()=>product:$body")
  }

  override fun deleteProductById(
    productId: String,
    request: ServiceRequest,
    resultHandler: Handler<AsyncResult<ServiceResponse>>
  ) {
    logger.info("deleteProductById()=>productId:$productId")
  }
}
