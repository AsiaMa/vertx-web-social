package com.oasis.social.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.ext.web.api.service.WebApiServiceGen;

@WebApiServiceGen
public interface IAuthService {
  static IAuthService create() {
    return new LoginServiceVerticle();
  }

  void generateToken(JsonObject body, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);
}
