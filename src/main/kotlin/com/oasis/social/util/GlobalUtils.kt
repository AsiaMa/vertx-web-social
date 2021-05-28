package com.oasis.social.util

import io.vertx.core.Context
import io.vertx.core.Vertx
import io.vertx.core.VertxException
import io.vertx.ext.web.Router
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.mysqlclient.MySQLPool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.SqlClient


/**
 * 从当前线程上下文获取Vertx
 * 如果不是Vertx线程则抛出异常
 */
fun getCurrentVertx(): Vertx {
  return if (Context.isOnVertxThread())
    Vertx.currentContext().owner()
  else
    throw VertxException("Current thread is not a Vertx thread")
}

class GlobalRouter {

  companion object {
    @Volatile
    private var INSTANCE: Router? = null

    fun getRouter(): Router =
      INSTANCE ?: synchronized(this) {
        INSTANCE ?: buildRouter(getCurrentVertx()).also { INSTANCE = it }
      }

    private fun buildRouter(vertx: Vertx) =
      Router.router(vertx)
  }
}

class GlobalMySqlClient {
  companion object {
    @Volatile
    private var INSTANCE: SqlClient? = null

    fun getClient(): SqlClient =
      INSTANCE ?: synchronized(this) {
        INSTANCE ?: buildClient().also { INSTANCE = it }
      }

    private fun buildClient(): SqlClient {
      val connectOptions = MySQLConnectOptions()
        .setPort(3306)
        .setHost("localhost")
        .setDatabase("web_social")
        .setUser("root")
        .setPassword("123456")
      // Pool options
      val poolOptions = PoolOptions()
        .setMaxSize(10)

      // Create the client pool
      return MySQLPool.pool(connectOptions, poolOptions)
    }
  }
}
