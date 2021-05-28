package com.oasis.social.service;

import com.oasis.social.models.User;
import com.oasis.social.persistence.IUserPersistence;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.ext.web.api.service.WebApiServiceGen;

/**
 * WebApiServiceGen注解会生成IUserServiceVertxProxyHandler代理类
 */
@WebApiServiceGen
public interface IUserService {

  static IUserService create(IUserPersistence userPersistence) {
    return new UserServiceImpl(userPersistence);
  }

  void getUserList(ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void getUserById(String userId, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void createUser(User body, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void updateUser(User body, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void deleteUserById(String userId, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);
}
