package com.oasis.social.driver

import com.oasis.social.config.appConfig
import io.vertx.mysqlclient.MySQLConnectOptions

object DbSettings {
  val mySQLConnectOptions: MySQLConnectOptions by lazy {
    MySQLConnectOptions()
      .setPort(appConfig.getJsonObject("db").getInteger("port"))
      .setHost(appConfig.getJsonObject("db").getString("host"))
      .setDatabase(appConfig.getJsonObject("db").getString("database_name"))
      .setUser(appConfig.getJsonObject("db").getString("username"))
      .setPassword(appConfig.getJsonObject("db").getString("password"))
  }
}
