package com.oasis.social.service;

import com.oasis.social.models.Product;
import com.oasis.social.persistence.IProductPersistence;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.ext.web.api.service.WebApiServiceGen;

@WebApiServiceGen
public interface IProductService {

  static IProductService create(IProductPersistence productPersistence) {
    return new ProductServiceVerticle(productPersistence);
  }

  void getProductList(ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void getProductById(String productId, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void createProduct(Product body, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void updateProduct(Product body, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void deleteProductById(String productId, ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);
}
