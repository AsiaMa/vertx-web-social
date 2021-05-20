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

class JWTVerticle : AbstractVerticle() {
  private lateinit var router: Router

  // 设置 sign algorithm 和 secret
  private lateinit var pubSecKeyOptions: PubSecKeyOptions
  private lateinit var jwt: JWTAuth

  override fun start() {
    router = SingletonRouter.getInstance(vertx)
    pubSecKeyOptions = PubSecKeyOptions().setAlgorithm("HS256").setBuffer("tom123")
    jwt = JWTAuth.create(vertx, JWTAuthOptions().addPubSecKey(pubSecKeyOptions))

    router.get("/auth/newToken").handler(this::generateToken)
  }

  private fun generateToken(ctx: RoutingContext) {
    val authorities = mutableListOf<String>()

    for (authority in ctx.request().params().getAll("authority")) {
      authorities.add(authority)
    }

    // 生成 token
    val body = JsonObject().put("userId", 123456).put("userName", "bob")
    val jwtOptions = JWTOptions().setExpiresInMinutes(2).setPermissions(authorities)
    val token = jwt.generateToken(body, jwtOptions)

    ctx.response().putHeader("context-type", "text/plain")
    ctx.response().end(token)
  }
}
