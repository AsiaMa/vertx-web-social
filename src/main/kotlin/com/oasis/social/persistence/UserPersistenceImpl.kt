package com.oasis.social.persistence

import com.oasis.social.models.User
import com.oasis.social.util.getCurrentVertx
import com.oasis.social.util.getMySqlConnections
import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet
import io.vertx.sqlclient.templates.SqlTemplate
import org.apache.logging.log4j.LogManager
import java.util.*

class UserPersistenceImpl : IUserPersistence {
  private val logger = LogManager.getLogger(this::class.java)
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
//      .mapTo(UserRowMapper.INSTANCE)
      .mapTo(userMapper)
      .execute(null)
      .map { it.toList() }
  }

  override fun findUserById(userId: String): Future<User?> {
    val parameters: Map<String, Any> = Collections.singletonMap("user_number", userId)
    return SqlTemplate.forQuery(pool, "SELECT * FROM user WHERE user_number=#{user_number}")
      .mapTo(userMapper)
      .execute(parameters)
      .map { it.toList().first() }
  }

  override fun addUser(user: User):
    Future<RowSet<Row>> {
    return SqlTemplate.forQuery(
      pool,
      "INSERT INTO user VALUES(null, #{id}, #{accountName}, #{nickName}, #{password}, #{age})"
    )
      .mapFrom(User::class.java)
      .execute(user)
  }

  override fun updateUser(user: User) {
    TODO("Not yet implemented")
  }

  override fun deleteUserById(userId: String) {
    TODO("Not yet implemented")
  }
}
