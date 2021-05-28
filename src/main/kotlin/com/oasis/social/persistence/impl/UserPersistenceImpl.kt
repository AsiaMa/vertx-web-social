package com.oasis.social.persistence.impl

import com.oasis.social.models.User
import com.oasis.social.persistence.IUserPersistence
import com.oasis.social.util.getCurrentVertx
import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.templates.SqlTemplate

class UserPersistenceImpl : IUserPersistence {
//  private val client = GlobalMySqlClient.getClient()

  private val userMapper = { row: Row ->
    val json = JsonObject()
      .put("id", row.getString("user_number"))
      .put("accountName", row.getString("account_name"))
      .put("nickName", row.getString("nick_name"))
      .put("password", row.getString("password"))
      .put("age", row.getInteger("age"))
    val user = User(json)
    user
  }

  override fun findUsers(): Future<Collection<User>> {
    val connectOptions = MySQLConnectOptions()
      .setPort(3306)
      .setHost("localhost")
      .setDatabase("web_social")
      .setUser("root")
      .setPassword("123456")
    val pool = Pool.pool(getCurrentVertx(), connectOptions, PoolOptions().setMaxSize(4))
    return SqlTemplate.forQuery(pool, "SELECT *  FROM user")
      .mapTo(userMapper).execute(null).map { it.toList() }
  }

  override fun findUserById(userId: String): User? {
    TODO("Not yet implemented")
  }

  override fun addUser(user: User) {
    TODO("Not yet implemented")
  }

  override fun updateUser(user: User) {
    TODO("Not yet implemented")
  }

  override fun deleteUserById(userId: String) {
    TODO("Not yet implemented")
  }
}
