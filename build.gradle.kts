plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor) version "2.3.3"
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.detekt)
}

group = "org.teamvoided"
version = "0.0.1"

detekt {
    allRules = true
    config.setFrom(file("config/detekt/detekt.yml"))

}


application {
    mainClass.set("org.teamvoided.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

sqldelight {
    databases {
        create("SqlDelight") {
            packageName = "org.teamvoided.database"
            dialect(libs.sqldelight.postgresql.get())
        }
    }
}

dependencies {
    implementation(libs.arrow.core)
    implementation(libs.arrow.optics)
    implementation(libs.arrow.fx.coroutines)


    implementation(libs.ktor.server.core.jvm)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation.jvm)
    implementation(libs.ktor.serialization.kotlinx.json.jvm)

    implementation(libs.ktor.server.html.builder)
    implementation(libs.kotlin.css)


    implementation(libs.postgresql)
    implementation(libs.jdbc.driver)
    implementation(libs.sqldelight.postgresql)
    implementation(libs.hikari)


    implementation(libs.h2)
    implementation(libs.logback)


    testImplementation(libs.ktor.server.tests.jvm)
    testImplementation(libs.kotlin.test.junit)

    implementation(libs.semver)

}
