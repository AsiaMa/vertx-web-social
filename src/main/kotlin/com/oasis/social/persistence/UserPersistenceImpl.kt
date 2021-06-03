package com.oasis.social.persistence

import com.oasis.social.models.User
import com.oasis.social.models.UserRowMapper
import com.oasis.social.util.getCurrentVertx
import com.oasis.social.util.getMySqlConnections
import io.vertx.core.Future
import io.vertx.sqlclient.*
import io.vertx.sqlclient.templates.SqlTemplate
import java.util.*

class UserPersistenceImpl : IUserPersistence {
  private val pool: Pool = Pool.pool(getCurrentVertx(), getMySqlConnections(), PoolOptions().setMaxSize(4))

  override fun findUsers(): Future<List<User>> {
    return SqlTemplate.forQuery(pool, "SELECT *  FROM user")
      .mapTo(UserRowMapper.INSTANCE)
      .execute(null)
      .map { it.toList() }
  }

  override fun findUserById(userId: String): Future<User?> {
    val parameters: Map<String, Any> = Collections.singletonMap("user_number", userId)
    return SqlTemplate.forQuery(pool, "SELECT * FROM user WHERE user_number=#{user_number}")
      .mapTo(UserRowMapper.INSTANCE)
      .execute(parameters)
      .map { it.toList().first() }
  }

  override fun addUser(user: User): Future<RowSet<Row>> {
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

  override fun deleteUserById(userId: String): Future<SqlResult<Void>> {
    val parameters: Map<String, Any> = Collections.singletonMap("user_number", userId)
    return SqlTemplate.forUpdate(pool, "DELETE FROM user WHERE user_number=#{user_number}")
      .execute(parameters)
  }
}
