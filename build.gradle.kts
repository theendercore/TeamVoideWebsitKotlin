val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

val postgres_version: String by project
val h2_version: String by project
plugins {
    application
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

group = "org.teamvoided"
version = "0.0.1"

application {
    mainClass.set("org.teamvoided.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

val arrow = "1.2.1"
val sqldelight = "2.0.0"
val semver_version = "1.4.2"

dependencies {
    implementation("io.arrow-kt", "arrow-core", arrow)
    implementation("io.arrow-kt", "arrow-optics", arrow)
    implementation("io.arrow-kt", "arrow-fx-coroutines", arrow)


    implementation("io.ktor", "ktor-server-core-jvm", ktor_version)
    implementation("io.ktor", "ktor-server-netty", ktor_version)
    implementation("io.ktor", "ktor-server-content-negotiation-jvm", ktor_version)
    implementation("io.ktor", "ktor-serialization-kotlinx-json-jvm", ktor_version)

    implementation("io.ktor", "ktor-server-html-builder", ktor_version)
    implementation("org.jetbrains.kotlin-wrappers", "kotlin-css", "1.0.0-pre.705")



    implementation("org.postgresql", "postgresql", postgres_version)
    implementation("app.cash.sqldelight", "jdbc-driver", sqldelight)
    implementation("app.cash.sqldelight", "postgresql-dialect", sqldelight)


    implementation("com.h2database", "h2", h2_version)
    implementation("ch.qos.logback", "logback-classic", logback_version)



    testImplementation("io.ktor", "ktor-server-tests-jvm", ktor_version)
    testImplementation("org.jetbrains.kotlin", "kotlin-test-junit", kotlin_version)

    implementation("io.github.z4kn4fein","semver",semver_version)

}
