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
import kotlinx.css.backgroundColor
import kotlinx.css.color
import kotlinx.html.*
import org.teamvoided.env.Env
import org.teamvoided.temp.makeTestPage
import org.teamvoided.util.assetFile
import org.teamvoided.util.respondBody
import org.teamvoided.util.respondCss

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

    get("/debug") {
        call.respond("Hello $this. ")
    }

    staticResources("/static", "assets")

    get("/") {
        call.respondHtml(HttpStatusCode.OK) { basicPage { home() } }
    }

    route("/voided-tweaks") {
        get {
            call.respondHtml { basicPage { voidedTweaks() } }
        }
        get("/data") {
            call.respondHtml { basicPage { voidedTweaks() } }
        }
        get("/crafting") {
            call.respondHtml { basicPage { voidedTweaks(VoidedTweaksRoutes.CRAFTING) } }
        }
        get("/resource") {
            call.respondHtml { basicPage { voidedTweaks(VoidedTweaksRoutes.RESOURCE) } }

        }
    }

    route("/triminator") {
        get { call.respondHtml { basicPage { triminator() } } }
    }

    get("/test") {
        call.respondHtml { basicPage { test() } }
    }

    route("/cmp") {

        route("/") {
            get { call.respondBody { home() } }
        }

        route("/voided-tweaks") {
            get {
                call.respondBody { voidedTweaks() }
            }
            get("/data") {
                call.respondBody { voidedTweaks() }
            }
            get("/crafting") {
                call.respondBody { voidedTweaks(VoidedTweaksRoutes.CRAFTING) }
            }
            get("/resource") {
                call.respondBody { voidedTweaks(VoidedTweaksRoutes.RESOURCE) }
            }

        }

        get("/test") {
            call.respondBody { test() }
        }

        get("/triminator") {
            call.respondBody { triminator() }
        }

        get("/*") {
            call.respondBody { errorPage() }
        }
    }

    get("/*") {
        call.respondHtml { basicPage { errorPage() } }
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

fun HTML.basicPage(content: FlowContent.() -> Unit = {}) {
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

    body("bg-bg text-text h-full") {
        nav(MainRoutes.entries.filter { it != MainRoutes.ERROR })
        div("h-full") {
            id = "main-content"
            content()
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

fun FlowContent.voidedTweaks(page: VoidedTweaksRoutes = VoidedTweaksRoutes.DATA) {
    nav(VoidedTweaksRoutes.entries)
    h1 {
        +when (page) {
            VoidedTweaksRoutes.DATA -> "Hello from DATA"
            VoidedTweaksRoutes.CRAFTING -> "Hello CRAFTING"
            VoidedTweaksRoutes.RESOURCE -> "Hello from RESOURCE"
        }
    }
}

fun FlowContent.test() {
    makeTestPage()
}

fun FlowContent.errorPage() {
    div("flex flex-col w-full h-[90vh] items-center justify-center gap-5") {
        h1("text-xl font-semibold") {
            +"404: Page Not Found"
        }
        img("funy yellow dispier", "https://media.tenor.com/UdO_ASApr9wAAAAC/emojedie.gif", "w-64 rounded-3xl")
    }
}

fun FlowContent.triminator() {
    iframe(null, "w-full h-[90vh]") {
        src = "https://triminator.vercel.app/"
        title = "Triminator embed"
    }
}

fun FlowContent.nav(
    tabs: List<ArbitraryRout>,
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
                    attributes["hx-get"] = "/cmp${it.url()}"
                    attributes["hx-push-url"] = it.url()
                    attributes["hx-target"] = "#main-content"
                    +it.label()
                }
            }
        }
    }
}

enum class MainRoutes(val label: String, val url: String) : ArbitraryRout {
    HOME("Home", "/"),
    VOIDED_TWEAKS("Voided Tweaks", "/voided-tweaks"),
    TRIMINATIOR("Triminator", "/triminator"),
    TEST("Test", "/test"),
    ADMIN("Admin", "/admin"),
    ERROR("Error", "/*");

    override fun label() = this.label
    override fun url() = this.url
}


enum class VoidedTweaksRoutes(val label: String, val url: String) : ArbitraryRout {
    DATA("Data Packs", "/data"),
    CRAFTING("Crafting Tweaks", "/crafting"),
    RESOURCE("Resource Packs", "/resource");

    override fun label() = this.label
    override fun url() = "/voided-tweaks" + this.url
}

interface ArbitraryRout {
    fun label(): String
    fun url(): String
}
