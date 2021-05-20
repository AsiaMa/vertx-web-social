package com.oasis.vertx_web_social

import com.oasis.vertx_web_social.common.SingletonRouter
import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ProductVerticle : AbstractVerticle() {
  private val logger: Logger = LogManager.getLogger(ProductVerticle::class.java)
  private lateinit var router: Router

  override fun start() {
    router = SingletonRouter.getInstance(vertx)
    logger.debug("router reference: $router")
    router.route("/api/product").handler { ctx ->
      ctx.response().putHeader("content-type", "application/json")
      ctx.json("Big computer，userId: ${ctx.get<Int>("userId")}，userName: ${ctx.get<String>("userName")}")
    }

    vertx.createHttpServer().requestHandler(router).listen(8888) { ar ->
      if (ar.succeeded()) {
        println("success ${ar.result()}")
      } else {
        println(ar.cause())
      }
    }
  }
}
