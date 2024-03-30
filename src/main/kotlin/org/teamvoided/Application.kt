package org.teamvoided

import arrow.fx.coroutines.resourceScope
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.awaitCancellation
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

fun Application.app(module: Dependencies) {
    Routing(module)
}
