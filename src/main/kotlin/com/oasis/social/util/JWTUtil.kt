package com.oasis.social.util

import com.oasis.social.config.appConfig
import io.vertx.core.buffer.Buffer
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.kotlin.ext.auth.jwt.jwtAuthOptionsOf
import io.vertx.kotlin.ext.auth.pubSecKeyOptionsOf

object JWTUtil {
  const val PERMISSIONS = "permissions"

  fun getJWTAuth(): JWTAuth {
    return JWTAuth.create(
      getCurrentVertx(),
      jwtAuthOptionsOf(
        pubSecKeys = listOf(
          pubSecKeyOptionsOf(
            algorithm = appConfig.getJsonObject("jwt").getString("algorithm"),
            buffer = Buffer.buffer(appConfig.getJsonObject("jwt").getString("secret"))
          )
        )
      )
    )
  }
}
