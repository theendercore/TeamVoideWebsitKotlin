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
import org.teamvoided.util.AssetFile

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
        call.respondHtml(HttpStatusCode.OK) { index(MyRoute.HOME) }
    }

    route("/voided-tweaks") {
        get() { call.respondHtml { index(MyRoute.VOIDED_TWEAKS) } }
    }
    get("/test") {
        call.respondHtml { index(MyRoute.TEST) }
    }


    get("/*") {
        call.respondHtml { index(MyRoute.ERROR) }
    }

    route("/components") {
        get("/image") {
            call.respondHtml { body { h1 { +"hi" } } }
        }
        get("/clicked") {
            call.respondHtml { body { +"Hello there 2" } }
        }
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

fun HTML.index(route: MyRoute) {
    head {
        title("TeamVoided Site :)")
        link {
            rel = "stylesheet"
            href = "/styles.css"
            type = "text/css"
        }
        script { src = "https://cdn.tailwindcss.com" }
        script { src = "https://unpkg.com/htmx.org@1.9.10" }
    }

//    File("src/main/resources/assets").walk().forEach(::println)

    body {
        classes += "bg-bg text-text"
        nav(mapOf("Home" to "", "Voided Tweaks" to "voided-tweaks", "Test" to "test"))

        when (route) {
            MyRoute.HOME -> {
                div {
                    classes += "flex h-[90vh] flex-col items-center justify-center gap-8"
                    img {
                        src = AssetFile("logo.png").toString()
                        alt = "TeamVoided logo"
                        width = "800"
                    }
                    h1 {
                        classes += "text-xl font-light italic"
                        +"\"The best god dam space crab pirates you have ever seen!\""
                    }
                }
            }

            MyRoute.VOIDED_TWEAKS -> {
                nav(
                    mapOf(
                        "Data" to "voided-tweaks/data",
                        "Resource" to "voided-tweaks/resource",
                        "Crafting" to "voided-tweaks/craft"
                    )
                )
                h1 { +"Hello from vt" }
            }

            MyRoute.TEST -> {
                div {
                    classes += "flex h-[90vh] flex-col items-center justify-center gap-8"
                    h1 {
                        classes += "text-xl"
                        +"TEST"
                    }
                }
            }

            MyRoute.ERROR -> {
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
        }

    }
}


fun BODY.nav(tabs: Map<String, String>) {
    header {
        classes += "w-full px-10 py-2 flex-col flex"
        nav {
            classes += "w-full flex items-center justify-center gap-16 text-xl"
            tabs.forEach {
                a {
                    classes += "hover:underline hover:scale-110"
                    href = "/${it.value}"
                    +it.key
                }
            }
        }
    }
}

enum class MyRoute { HOME, VOIDED_TWEAKS, TEST, ERROR }