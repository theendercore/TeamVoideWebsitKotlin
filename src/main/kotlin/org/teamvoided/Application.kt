package org.teamvoided

import arrow.fx.coroutines.resourceScope
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.coroutines.awaitCancellation
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import org.teamvoided.env.Dependencies
import org.teamvoided.env.Env
import org.teamvoided.env.dependencies
import org.teamvoided.pages.Routing

suspend fun main() {
    val env = Env()
    resourceScope {
        val dependencies = dependencies(env)
        embeddedServer(Netty, port = env.http.port, host = env.http.host) { app(dependencies) }
            .start(wait = true)
        awaitCancellation()
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun Application.app(module: Dependencies) {
    install(ContentNegotiation) {
        json(Json {
            namingStrategy = JsonNamingStrategy.SnakeCase
        })
    }
    Routing(module)
}
