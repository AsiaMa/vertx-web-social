package com.oasis.social.util

import io.vertx.ext.auth.authorization.PermissionBasedAuthorization
import io.vertx.ext.web.RoutingContext

fun RoutingContext.requiredRole(name: String): Boolean {
  val adminPermission = PermissionBasedAuthorization.create(name)
  if (!adminPermission.match(this.user())) {
    this.end("没有权限")
    return false
  }
  return true
}
