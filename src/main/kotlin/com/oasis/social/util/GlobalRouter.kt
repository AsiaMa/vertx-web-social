package com.oasis.social.util

import io.vertx.core.Context
import io.vertx.core.Vertx
import io.vertx.core.VertxException
import io.vertx.ext.web.Router
import io.vertx.mysqlclient.MySQLConnectOptions


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

/**
 * get mysql connection
 */
fun getMySqlConnections(): MySQLConnectOptions {
//  val store =
//    configStoreOptionsOf(type = "file", format = "hocon", config = JsonObject().put("path", "db/mysql-connection.conf"))
//  val configRetriever = ConfigRetriever.create(getCurrentVertx(), ConfigRetrieverOptions().addStore(store))
//  val config = configRetriever.config.await()
  return MySQLConnectOptions()
//    .setPort(config.getJsonObject("db").getInteger("port"))
    .setPort(3306)
//    .setHost(config.getJsonObject("db").getString("host"))
    .setHost("localhost")
//    .setDatabase(config.getJsonObject("db").getString("database_name"))
    .setDatabase("web_social")
//    .setUser(config.getJsonObject("db").getString("username"))
    .setUser("username")
//    .setPassword(config.getJsonObject("db").getString("password"))
    .setPassword("password")
}
