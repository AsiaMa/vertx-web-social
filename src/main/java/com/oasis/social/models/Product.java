package com.oasis.social.models;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true, publicConverter = false)
public class Product {
  private String id;
  private String name;
  private Double price;
  private Integer stock;

  public Product(String id, String name, Double price, Integer stock) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.stock = stock;
  }

  public Product(Product other) {
    this.id = other.getId();
    this.name = other.getName();
    this.price = other.getPrice();
    this.stock = other.getStock();
  }

  public Product(JsonObject json) {
    ProductConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    ProductConverter.toJson(this, json);
    return json;
  }

  public String getId() {
    return id;
  }

  @Fluent
  public Product setId(String id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return this.name;
  }

  @Fluent
  public Product setName(String name) {
    this.name = name;
    return this;
  }

  public Double getPrice() {
    return this.price;
  }

  @Fluent
  public Product setPrice(Double price) {
    this.price = price;
    return this;
  }

  public Integer getStock() {
    return this.stock;
  }

  @Fluent
  public Product setStock(Integer stock) {
    this.stock = stock;
    return this;
  }

  @Override
  public String toString() {
    return "Product{" +
      "id='" + id + '\'' +
      ", name='" + name + '\'' +
      ", price=" + price +
      ", stock=" + stock +
      '}';
  }
}
