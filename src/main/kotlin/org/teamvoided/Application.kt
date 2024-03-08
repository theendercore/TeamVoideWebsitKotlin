package org.teamvoided

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.css.Color
import kotlinx.css.CssBuilder
import kotlinx.css.backgroundColor
import kotlinx.css.color
import kotlinx.html.*
import org.teamvoided.env.Env
import org.teamvoided.util.assetFile

fun main() {
    val env = Env()

    embeddedServer(Netty, port = env.http.port, host = env.http.host) {
        routing()
//        configureDatabases()
    }.start(wait = true)
}


fun Application.routing() = routing {

    get("/health-check") {
        call.respond(HttpStatusCode.OK)
    }

    staticResources("/static", "assets")

    get("/") {
        call.respondHtml(HttpStatusCode.OK) { index(MyRoutes.HOME) }
    }

    route("/voided-tweaks") {
        get { call.respondHtml { index(MyRoutes.VOIDED_TWEAKS) } }
    }

    get("/test") {
        call.respondHtml { index(MyRoutes.TEST) }
    }

    route("/cmp") {

        route("/") {
            get { call.respondHtml { body { home() } } }
        }

        route("/voided-tweaks") {
            get { call.respondHtml { body { voidedTweaks() } } }
        }

        get("/test") {
            call.respondHtml { body { test() } }
        }
        get("/*") {
            call.respondHtml { body { error() } }
        }
    }

    get("/*") {
        call.respondHtml { index(MyRoutes.ERROR) }
    }

    get("/styles.css") {
        call.respondCss {
            rule(".bg-bg") {
                backgroundColor = Color.black
            }
            rule(".text-text") {
                color = Color("#DDD6FE")
            }
        }
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

fun HTML.index(route: MyRoutes) {
    head {
        title("TeamVoided Site")
        link {
            rel = "stylesheet"
            href = "/styles.css"
            type = "text/css"
        }
        script { src = "https://cdn.tailwindcss.com" }
        script { src = "https://unpkg.com/htmx.org@1.9.10" }
    }

    body {
        classes += "bg-bg text-text"
        nav(MyRoutes.entries.filter { it != MyRoutes.ERROR })
        div {
            id = "main-content"
            when(route){
                MyRoutes.HOME -> home()
                MyRoutes.VOIDED_TWEAKS -> voidedTweaks()
                MyRoutes.TEST -> test()
                MyRoutes.ERROR -> error()
            }
        }
    }
}


fun FlowContent.nav(
    tabs: List<MyRoutes>,
    header: String = "w-full px-10 py-2 flex-col flex",
    nav: String = "w-full flex items-center justify-center gap-16 text-xl",
    a: String = "hover:underline hover:scale-110"
) {
    header {
        classes += header
        nav {
            classes += nav
            tabs.forEach {
                button {
                    classes += a
                    attributes["hx-get"] = "/cmp${it.url}"
                    attributes["hx-push-url"] = it.url
                    attributes["hx-target"] = "#main-content"
                    +it.visName
                }
            }
        }
    }
}

fun FlowContent.home() {
    div {
        classes += "flex h-[90vh] flex-col items-center justify-center gap-8"
        img {
            src = assetFile("logo.png")
            alt = "TeamVoided logo"
            width = "800"
        }
        h1 {
            classes += "text-xl font-light italic"
            +"\"The best god dam space crab pirates you have ever seen!\""
        }
    }
}

fun FlowContent.voidedTweaks() {
//    nav(
//        mapOf(
//            "Data" to "voided-tweaks/data",
//            "Resource" to "voided-tweaks/resource",
//            "Crafting" to "voided-tweaks/craft"
//        )
//    )
    h1 { +"Hello from vt" }
}

fun FlowContent.test() {
    div {
        classes += "flex h-[90vh] flex-col items-center justify-center gap-8"
        h1 {
            classes += "text-xl"
            +"TEST"
        }
    }
}

fun FlowContent.error() {
    div {
        classes += "flex flex-col w-full h-[90vh] items-center justify-center gap-5"
        h1 {
            classes += "text-xl font-semibold"
            +"404: Page Not Found"
        }
        img {
            classes += "w-64 rounded-3xl"
            src = "https://media.tenor.com/UdO_ASApr9wAAAAC/emojedie.gif"
            alt = "funy yellow dispier"
        }
    }
}

enum class MyRoutes(val visName: String, val url: String) {
    HOME("Home", "/"),
    VOIDED_TWEAKS("Voided Tweaks", "/voided-tweaks"),
    TEST("Test", "/test"),
    ERROR("Error", "/*");
}


