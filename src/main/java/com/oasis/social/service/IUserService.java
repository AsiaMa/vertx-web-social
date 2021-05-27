package com.oasis.social.service;

import com.oasis.social.models.User;
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

  static IUserService create() {
    return new UserServiceImpl();
  }

  void getUserList(ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void getUserById(String userId, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void createUser(User user, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void updateUser(User user, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void deleteUserById(String userId, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);
}
