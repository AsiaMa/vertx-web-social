package com.oasis.social.persistence;

import com.oasis.social.models.Product;
import io.vertx.core.Future;

import java.util.List;

public interface IProductPersistence {
  static IProductPersistence create() {
    return new ProductPersistenceImpl();
  }

  Future<List<Product>> findProducts();

  Future<Product> findProductById(Integer productId);

  Future<Void> addProduct(Product product);

  Future<Void> updateProduct(Integer id, Product product);

  Future<Void> deleteProductById(Integer productId);
}
