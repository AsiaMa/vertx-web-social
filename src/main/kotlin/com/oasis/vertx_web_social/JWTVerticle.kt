package com.oasis.vertx_web_social

import com.oasis.vertx_web_social.common.SingletonRouter
import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.JWTOptions
import io.vertx.ext.auth.PubSecKeyOptions
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.auth.jwt.JWTAuthOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.JWTAuthHandler
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


class JWTVerticle : AbstractVerticle() {
  private val logger: Logger = LogManager.getLogger(JWTVerticle::class.java)

  private lateinit var router: Router

  // 设置 sign algorithm 和 secret
  private lateinit var pubSecKeyOptions: PubSecKeyOptions
  private lateinit var jwt: JWTAuth

  override fun start() {
    router = SingletonRouter.getInstance(vertx)
    logger.debug("router reference: $router")
    pubSecKeyOptions = PubSecKeyOptions().setAlgorithm("HS256").setBuffer("tom123")
    jwt = JWTAuth.create(vertx, JWTAuthOptions().addPubSecKey(pubSecKeyOptions))

    router.route().handler(BodyHandler.create())
    router.post("/auth/newToken").handler(this::generateToken)
    // protect the api
    router.route("/api/*").handler(JWTAuthHandler.create(jwt))
    // 拦截 api/* 接口，解析 token
    router.route("/api/*").handler(this::parseToken)
  }

  private fun generateToken(ctx: RoutingContext) {
    val authorities = mutableListOf<String>()
    val authority = ctx.request().getParam("authority")
    logger.debug("authorities: $authority")
    for (item in authority.split(",")) {
      authorities.add(item.trim())
    }

    // 生成 token
    val body = JsonObject().put("userId", 123456).put("userName", "Bob")
    val jwtOptions = JWTOptions().setExpiresInMinutes(2).setPermissions(authorities)
    val token = jwt.generateToken(body, jwtOptions)

    ctx.response().putHeader("context-type", "text/plain")
    ctx.response().end(token)
  }

  private fun parseToken(ctx: RoutingContext) {
    val user = ctx.user()
    val userId = user.principal().getInteger("userId")
    val userName = user.principal().getString("userName")

    ctx.put("userId", userId)
    ctx.put("userName", userName)
    ctx.next()
  }
}
