package com.oasis.vertx_web_social

import com.oasis.vertx_web_social.common.SingletonRouter
import io.vertx.core.AbstractVerticle
import io.vertx.ext.auth.authorization.PermissionBasedAuthorization
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ProductVerticle : AbstractVerticle() {
  private val logger: Logger = LogManager.getLogger(ProductVerticle::class.java)
  private lateinit var router: Router

  override fun start() {
    router = SingletonRouter.getInstance(vertx)
    logger.debug("router reference: $router")
    router.route("/api/product").handler(this::getProduct)

    startHttpServer()
  }

  private val adminPermission = PermissionBasedAuthorization.create("admin")

  private fun getProduct(ctx: RoutingContext) {
    if (adminPermission.match(ctx.user())) {
      ctx.json("Big computer，userId: ${ctx.get<Int>("userId")}，userName: ${ctx.get<String>("userName")}")
    } else {
      ctx.end("没有权限")
    }
  }

  private fun startHttpServer() {
    vertx.createHttpServer().requestHandler(router).listen(8888) { ar ->
      if (ar.succeeded()) {
        println("success ${ar.result()}")
      } else {
        println(ar.cause())
      }
    }
  }
}
