package com.oasis.social.persistence

import com.oasis.social.models.Product
import com.oasis.social.util.getCurrentVertx
import com.oasis.social.util.getMySqlConnections
import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import io.vertx.sqlclient.*
import io.vertx.sqlclient.templates.SqlTemplate
import org.apache.logging.log4j.LogManager
import java.util.*

class ProductPersistenceImpl : IProductPersistence {
  private val logger = LogManager.getLogger(this::class.java)
  private val pool: Pool = Pool.pool(getCurrentVertx(), getMySqlConnections(), PoolOptions().setMaxSize(4))

  private val productMapper = { row: Row ->
    val json = JsonObject()
      .put("id", row.getInteger("id"))
      .put("name", row.getString("name"))
      .put("price", row.getDouble("price"))
      .put("stock", row.getInteger("stock"))
    val product = Product(json)
    product
  }

  override fun findProducts(): Future<Collection<Product>> {
    return SqlTemplate.forUpdate(pool, "SELECT * FROM product")
      .mapTo(productMapper)
      .execute(null)
      .map { it.toList() }
  }

  override fun findProductById(productId: String): Future<Product> {
    val parameters: Map<String, Any> = Collections.singletonMap("id", productId)
    return SqlTemplate.forQuery(pool, "SELECT * FROM product WHERE id = #{id}")
      .mapTo(productMapper)
      .execute(parameters)
      .map { it.toList().first() }
  }

  override fun addProduct(product: Product): Future<RowSet<Row>> {
    return SqlTemplate.forQuery(pool, "INSERT INTO product VALUES(#{id}, #{name}, #{price}, #{stock})")
      .mapFrom(Product::class.java)
      .execute(product)
  }

  override fun updateProduct() {
    TODO("Not yet implemented")
  }

  override fun deleteProductById(productId: String): Future<SqlResult<Void>> {
    val parameters: Map<String, Any> = Collections.singletonMap("id", productId)
    return SqlTemplate.forUpdate(pool, "DELETE FROM product WHERE id=#{id}")
      .execute(parameters)
  }

}
