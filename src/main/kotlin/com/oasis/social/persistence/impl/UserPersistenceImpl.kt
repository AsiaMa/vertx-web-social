package com.oasis.social.persistence.impl

import com.oasis.social.models.User
import com.oasis.social.persistence.IUserPersistence
import com.oasis.social.util.getCurrentVertx
import com.oasis.social.util.getMySqlConnections
import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.templates.SqlTemplate

class UserPersistenceImpl : IUserPersistence {
  private val pool: Pool = Pool.pool(getCurrentVertx(), getMySqlConnections(), PoolOptions().setMaxSize(4))

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
