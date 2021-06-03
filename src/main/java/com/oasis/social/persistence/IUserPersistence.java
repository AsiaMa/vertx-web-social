package com.oasis.social.persistence;

import com.oasis.social.models.User;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlResult;

import java.util.List;

public interface IUserPersistence {
  static IUserPersistence create() {
    return new UserPersistenceImpl();
  }

  Future<List<User>> findUsers();

  Future<User> findUserById(String userId);

  Future<RowSet<Row>> addUser(User user);

  void updateUser(User user);

  Future<SqlResult<Void>> deleteUserById(String userId);
}
