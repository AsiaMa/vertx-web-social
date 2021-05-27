package com.oasis.social

import com.oasis.social.common.GlobalRouter
import com.oasis.social.util.requiredRole
import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import org.apache.logging.log4j.LogManager

@Deprecated(message = "0.1")
class ProductVerticle : AbstractVerticle() {
  private val logger = LogManager.getLogger(this::class.java)
  private lateinit var router: Router

  override fun start() {
    router = GlobalRouter.getRouter()
    logger.debug("router reference: $router")
    router.route("/api/product").handler(this::getProduct)

    startHttpServer()
  }

  private fun getProduct(ctx: RoutingContext) {
    if (ctx.requiredRole("admin")) {
      ctx.json("Big computer，userId: ${ctx.get<Int>("userId")}，userName: ${ctx.get<String>("userName")}")
    }
  }

  private fun startHttpServer() {
    vertx.createHttpServer().requestHandler(router).listen(8888) { ar ->
      if (ar.succeeded()) {
        logger.info("success ${ar.result()}")
      } else {
        logger.error(ar.cause())
      }
    }
  }
}
