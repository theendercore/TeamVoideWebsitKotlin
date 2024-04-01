package org.teamvoided.util

import io.github.z4kn4fein.semver.toVersion
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.css.CssBuilder
import kotlinx.html.FlowContent
import kotlinx.html.body
import kotlin.io.path.Path

const val DEFAULT_ICON = "https://minecraft.wiki/images/Default_pack.png?d7150"
fun assetFile(path: String) = Path("/static/$path").toString()
fun jsFile(path: String) = Path("/static/js/$path.js").toString()
fun Version(version: String) = version.toVersion()
fun format(str: String) = str.trim().replace(Regex("[^A-Za-z]"), "-")

suspend fun RoutingCall.respondBody(status: HttpStatusCode = HttpStatusCode.OK, block: FlowContent.() -> Unit) =
    this.respondHtml(status) { body { block() } }

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
