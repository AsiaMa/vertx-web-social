package com.oasis.social.driver

import io.vertx.sqlclient.SqlConnectOptions

interface SqlHelper {

  /**
   * 获得数据库连接
   */
  fun getConnection(): SqlConnectOptions
}
