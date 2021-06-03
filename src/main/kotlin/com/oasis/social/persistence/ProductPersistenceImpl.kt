package com.oasis.social.persistence

import com.oasis.social.models.Product
import com.oasis.social.models.ProductParametersMapper
import com.oasis.social.models.ProductRowMapper
import com.oasis.social.util.getCurrentVertx
import com.oasis.social.util.getMySqlConnections
import io.vertx.core.Future
import io.vertx.sqlclient.*
import io.vertx.sqlclient.templates.SqlTemplate
import java.util.*

class ProductPersistenceImpl : IProductPersistence {
  private val pool: Pool = Pool.pool(getCurrentVertx(), getMySqlConnections(), PoolOptions().setMaxSize(4))

  override fun findProducts(): Future<List<Product>> {
    return SqlTemplate.forUpdate(
      pool,
      "SELECT id, name, price, stock FROM product"
    )
      .mapTo(ProductRowMapper.INSTANCE)
      .execute(null)
      .map { it.toList() }
  }

  override fun findProductById(productId: String): Future<Product> {
    val parameters: Map<String, Any> = Collections.singletonMap("id", productId)
    return SqlTemplate.forQuery(
      pool,
      "SELECT id, name, price, stock FROM product WHERE id = #{id}"
    )
      .mapTo(ProductRowMapper.INSTANCE)
      .execute(parameters)
      .map { it.toList().firstOrNull() }
  }

  override fun addProduct(product: Product): Future<RowSet<Row>> {
    return SqlTemplate.forQuery(
      pool,
      "INSERT INTO product VALUES(#{id}, #{name}, #{price}, #{stock})"
    )
      .mapFrom(ProductParametersMapper.INSTANCE)
      .execute(product)
  }

  override fun updateProduct() {
    TODO("Not yet implemented")
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
