package com.oasis.social.persistence;

import com.oasis.social.models.Product;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlResult;

import java.util.List;

public interface IProductPersistence {
  static IProductPersistence create() {
    return new ProductPersistenceImpl();
  }

  Future<List<Product>> findProducts();

  Future<Product> findProductById(String productId);

  Future<RowSet<Row>> addProduct(Product product);

  void updateProduct();

  Future<SqlResult<Void>> deleteProductById(String productId);
}
