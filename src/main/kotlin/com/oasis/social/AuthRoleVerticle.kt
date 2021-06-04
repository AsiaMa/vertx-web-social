package com.oasis.social

import com.oasis.social.util.GlobalRouter
import io.vertx.ext.auth.authorization.PermissionBasedAuthorization
import io.vertx.ext.auth.jwt.authorization.JWTAuthorization
import io.vertx.ext.web.handler.AuthorizationHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle

class AuthRoleVerticle : CoroutineVerticle() {
  override suspend fun start() {
    val router = GlobalRouter.getRouter()

    val requireAdminHandler = AuthorizationHandler.create(PermissionBasedAuthorization.create("admin"))
      .addAuthorizationProvider(JWTAuthorization.create("permissions"))

    //需要admin权限
    router.route("/api/users").handler(requireAdminHandler)
  }
}
