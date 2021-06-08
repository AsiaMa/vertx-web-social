package com.oasis.social.driver.impl

import com.oasis.social.config.appConfig
import com.oasis.social.driver.SqlHelper
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.sqlclient.SqlConnectOptions

object MySqlHelper : SqlHelper {
  override fun getConnection(): SqlConnectOptions {
    return MySQLConnectOptions()
      .setPort(appConfig.getJsonObject("db").getInteger("port"))
      .setHost(appConfig.getJsonObject("db").getString("host"))
      .setDatabase(appConfig.getJsonObject("db").getString("database_name"))
      .setUser(appConfig.getJsonObject("db").getString("username"))
      .setPassword(appConfig.getJsonObject("db").getString("password"))
  }
}
