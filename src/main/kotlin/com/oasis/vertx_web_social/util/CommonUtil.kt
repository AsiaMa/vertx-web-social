package com.oasis.vertx_web_social.util

import io.vertx.ext.auth.authorization.PermissionBasedAuthorization
import io.vertx.ext.web.RoutingContext

fun RoutingContext.requiredRole(name: String): RoutingContext {
  val adminPermission = PermissionBasedAuthorization.create(name)
  if (!adminPermission.match(this.user())) {
    this.end("没有权限")
  }
  return this
}
