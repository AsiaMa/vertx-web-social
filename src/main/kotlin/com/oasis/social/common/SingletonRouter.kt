package com.oasis.social.common

import io.vertx.core.Vertx
import io.vertx.ext.web.Router


class SingletonRouter {

  companion object {
    @Volatile
    private var INSTANCE: Router? = null

    fun getInstance(vertx: Vertx): Router =
      INSTANCE ?: synchronized(this) {
        INSTANCE ?: buildRouter(vertx).also { INSTANCE = it }
      }

    private fun buildRouter(vertx: Vertx) =
      Router.router(vertx)
  }
}
