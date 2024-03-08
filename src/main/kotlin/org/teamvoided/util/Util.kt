package org.teamvoided.util

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.css.CssBuilder
import kotlinx.html.FlowContent
import kotlinx.html.body
import kotlin.io.path.Path

fun assetFile(path: String) = Path("static/$path").toString()
suspend fun RoutingCall.respondBody(status: HttpStatusCode = HttpStatusCode.OK, block: FlowContent.() -> Unit) =
    this.respondHtml(status) { body { block() } }

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
