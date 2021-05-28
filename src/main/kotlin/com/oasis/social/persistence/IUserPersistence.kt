package com.oasis.social.persistence

import com.oasis.social.models.User
import com.oasis.social.persistence.impl.UserPersistenceImpl
import io.vertx.core.Future

interface IUserPersistence {
  companion object {
    fun create(): IUserPersistence {
      return UserPersistenceImpl()
    }
  }

  fun findUsers(): Future<Collection<User>>

  fun findUserById(userId: String): User?

  fun addUser(user: User)

  fun updateUser(user: User)

  fun deleteUserById(userId: String)
}
