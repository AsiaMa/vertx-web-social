package com.oasis.social.persistence;

import com.oasis.social.models.User;
import io.vertx.core.Future;

import java.util.Collection;
import java.util.Optional;

public interface IUserPersistence {

  static IUserPersistence create() {
    return new UserPersistenceImpl();
  }

  Future<Collection<User>> findUsers();

  Optional<User> findUserById(String userId);

  void addUser(User user);

  void updateUser(User user);

  void deleteUserById(String userId);
}
