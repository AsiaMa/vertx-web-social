package com.oasis.social.config

import com.oasis.social.util.getCurrentVertx
import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.config.configStoreOptionsOf
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await

lateinit var appConfig: JsonObject

class AppConfigVerticle : CoroutineVerticle() {
  override suspend fun start() {
    val store =
      configStoreOptionsOf(type = "file", format = "hocon", config = JsonObject().put("path", "application.conf"))
    val configRetriever = ConfigRetriever.create(getCurrentVertx(), ConfigRetrieverOptions().addStore(store))
    appConfig = configRetriever.config.await()
  }
}
