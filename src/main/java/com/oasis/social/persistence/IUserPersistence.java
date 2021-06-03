package com.oasis.social.persistence;

import com.oasis.social.models.User;
import io.vertx.core.Future;

import java.util.List;

public interface IUserPersistence {
  static IUserPersistence create() {
    return new UserPersistenceImpl();
  }

  Future<List<User>> findUsers();

  Future<User> findUserById(String userId);

  Future<Void> addUser(User user);

  Future<Void> updateUser(String userId, User user);

  Future<Void> deleteUserById(String userId);
}
