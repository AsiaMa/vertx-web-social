import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.4.21"
  application
  id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.oasis"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.0.3"
val junitJupiterVersion = "5.7.0"

val mainVerticleName = "com.oasis.vertx_web_social.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClassName = launcherClassName
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation("io.vertx:vertx-lang-kotlin")
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-lang-kotlin-coroutines")
  // jwt
  implementation("io.vertx:vertx-auth-jwt")
  // web
  implementation("io.vertx:vertx-web")
  // mysql
  implementation("io.vertx:vertx-mysql-client")
  // codegen
  annotationProcessor("io.vertx:vertx-codegen:$vertxVersion:processor")
  compileOnly("io.vertx:vertx-codegen")
  // web api service
  annotationProcessor("io.vertx:vertx-web-api-service:$vertxVersion")
  implementation("io.vertx:vertx-web-api-service")
  // logging
  implementation("org.apache.logging.log4j:log4j-core:2.14.1")
  implementation("com.lmax:disruptor:3.4.4")
  // test
  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "11"

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf(
    "run",
    mainVerticleName,
    "--redeploy=$watchForChange",
    "--launcher-class=$launcherClassName",
    "--on-redeploy=$doOnChange"
  )
}
