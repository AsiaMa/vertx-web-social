package com.oasis.social.persistence

import com.oasis.social.models.Product
import com.oasis.social.models.ProductParametersMapper
import com.oasis.social.models.ProductRowMapper
import com.oasis.social.util.getCurrentVertx
import com.oasis.social.util.getMySqlConnections
import io.vertx.core.Future
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.SqlResult
import io.vertx.sqlclient.templates.SqlTemplate
import java.util.*

class ProductPersistenceImpl : IProductPersistence {
  private val pool: Pool = Pool.pool(getCurrentVertx(), getMySqlConnections(), PoolOptions().setMaxSize(4))

  override fun findProducts(): Future<List<Product>> {
    return SqlTemplate.forQuery(
      pool,
      "SELECT id, name, price, stock FROM product"
    )
      .mapTo(ProductRowMapper.INSTANCE)
      .execute(null)
      .map { it.toList() }
  }

  override fun findProductById(productId: Int): Future<Product?> {
    val parameters: Map<String, Any> = Collections.singletonMap("id", productId)
    return SqlTemplate.forQuery(
      pool,
      "SELECT id, name, price, stock FROM product WHERE id = #{id}"
    )
      .mapTo(ProductRowMapper.INSTANCE)
      .execute(parameters)
      .map { it.toList().firstOrNull() }
  }

  override fun addProduct(product: Product): Future<Void> {
    return SqlTemplate.forUpdate(
      pool,
      "INSERT INTO product VALUES(#{id}, #{name}, #{price}, #{stock})"
    )
      .mapFrom(ProductParametersMapper.INSTANCE)
      .execute(product)
      .flatMap { Future.succeededFuture() }
  }

  override fun updateProduct(productId: Int, product: Product): Future<Void> {
    return SqlTemplate.forUpdate(
      pool,
      "UPDATE product SET name=#{name}, price=#{price}, stock=#{stock} WHERE id=#{id}"
    ).mapFrom(ProductParametersMapper.INSTANCE)
      .execute(product)
      .flatMap { Future.succeededFuture() }
  }

  override fun deleteProductById(productId: String): Future<SqlResult<Void>> {
    val parameters: Map<String, Any> = Collections.singletonMap("id", productId)
    return SqlTemplate.forUpdate(
      pool,
      "DELETE FROM product WHERE id=#{id}"
    )
      .execute(parameters)
  }

}
