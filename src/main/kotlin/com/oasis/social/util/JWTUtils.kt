package com.oasis.social.util

import io.vertx.ext.auth.PubSecKeyOptions
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.auth.jwt.JWTAuthOptions

object JWTUtils {

  fun getJWTAuth(): JWTAuth {
    return JWTAuth.create(
      getCurrentVertx(),
      JWTAuthOptions().addPubSecKey(PubSecKeyOptions().setAlgorithm("HS256").setBuffer("Bob"))
    )
  }
}
