package com.oasis.social.persistence

import com.oasis.social.models.User
import com.oasis.social.models.UserParametersMapper
import com.oasis.social.models.UserRowMapper
import com.oasis.social.util.getCurrentVertx
import com.oasis.social.util.getMySqlConnections
import io.vertx.core.Future
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.templates.SqlTemplate
import java.util.*


class UserPersistenceImpl : IUserPersistence {
  private val pool: Pool = Pool.pool(getCurrentVertx(), getMySqlConnections(), PoolOptions().setMaxSize(4))

  override fun findUsers(): Future<List<User>> {
    return SqlTemplate.forQuery(
      pool,
      "SELECT id, user_number, account_name, nick_name, password, age  FROM user"
    )
      .mapTo(UserRowMapper.INSTANCE)
      .execute(null)
      .map { it.toList() }
  }

  override fun findUserById(userId: String): Future<User?> {
    val parameters: Map<String, Any> = Collections.singletonMap("user_number", userId)
    return SqlTemplate.forQuery(
      pool,
      "SELECT id, user_number, account_name, nick_name, password, age  FROM user WHERE user_number=#{user_number}"
    )
      .mapTo(UserRowMapper.INSTANCE)
      .execute(parameters)
      .map { it.toList().firstOrNull() }
  }

  override fun addUser(user: User): Future<Void> {
    return SqlTemplate.forUpdate(
      pool,
      "INSERT INTO user VALUES(null, #{userNumber}, #{accountName}, #{nickName}, #{password}, #{age})"
    )
      .mapFrom(UserParametersMapper.INSTANCE)
      .execute(user)
      .flatMap {
        Future.succeededFuture()
      }
  }

  override fun updateUser(id: String, user: User): Future<Void> {
    user.id = id
    return SqlTemplate.forUpdate(
      pool,
      "UPDATE user SET account_name=#{accountName}, nick_name=#{nickName}, password=#{password}, age=#{age} where user_number=#{userNumber}"
    )
      .mapFrom(UserParametersMapper.INSTANCE)
      .execute(user).flatMap {
        Future.succeededFuture()
      }
  }

  override fun deleteUserById(userId: String): Future<Void> {
    val parameters: Map<String, Any> = Collections.singletonMap("user_number", userId)
    return SqlTemplate.forUpdate(
      pool,
      "DELETE FROM user WHERE user_number=#{user_number}"
    )
      .execute(parameters)
      .flatMap { Future.succeededFuture() }
  }
}
