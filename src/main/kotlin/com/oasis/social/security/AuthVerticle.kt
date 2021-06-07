package com.oasis.social.security

import com.oasis.social.enum.RoleEnum
import com.oasis.social.util.GlobalRouter
import com.oasis.social.util.JWTUtils
import io.vertx.ext.auth.authorization.PermissionBasedAuthorization
import io.vertx.ext.auth.jwt.authorization.JWTAuthorization
import io.vertx.ext.web.handler.AuthorizationHandler
import io.vertx.ext.web.handler.JWTAuthHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle

class AuthVerticle : CoroutineVerticle() {
  override suspend fun start() {
    val router = GlobalRouter.getRouter()
    //对/api开头的所有接口进行用户认证
    router.route("/api/*")
      .handler(JWTAuthHandler.create(JWTUtils.getJWTAuth()))

    val requireAdminHandler = AuthorizationHandler.create(PermissionBasedAuthorization.create(RoleEnum.ADMIN.name))
      .addAuthorizationProvider(JWTAuthorization.create(JWTUtils.PERMISSIONS))

    //需要admin权限
    router.route("/api/users").handler(requireAdminHandler)
  }
}
