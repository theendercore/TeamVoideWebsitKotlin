package org.teamvoided.pages

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import kotlinx.css.color
import kotlinx.html.*
import org.teamvoided.env.Dependencies
import org.teamvoided.util.assetFile
import org.teamvoided.util.respondBody
import org.teamvoided.util.respondCss

fun Application.Routing(module: Dependencies) = routing {

    staticResources("/static", "assets")

    get("/health-check") { call.respond(HttpStatusCode.OK) }

    get("/") { call.respondHtml(HttpStatusCode.OK) { htmlWrapper { home() } } }

    voidedTweaksRout(module)

    adminPanelRout(module)

    get("/triminator") { call.respondHtml { htmlWrapper { triminator() } } }

    testRouting(module)

    route("/cmp") {
        route("/") { get { call.respondBody { home() } } }

        get("/triminator") { call.respondBody { triminator() } }

        get("/*") { call.respondBody { errorPage() } }
    }

    get("/*") { call.respondHtml { htmlWrapper { errorPage() } } }

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

fun HTML.htmlWrapper(content: FlowContent.() -> Unit = {}) {
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


fun FlowContent.errorPage() {
    div("flex flex-col w-full h-[90vh] items-center justify-center gap-5") {
        h1("text-xl font-semibold") { +"404: Page Not Found" }
        img("funy yellow dispier", classes = "w-64 rounded-3xl") {
            src = "https://media.tenor.com/UdO_ASApr9wAAAAC/emojedie.gif"
        }
    }
}

fun FlowContent.triminator() {
    iframe(null, "w-full h-[90vh]") {
        src = "https://triminator-git-dev-theendercore.vercel.app/embeded"
        title = "Triminator embed"
    }
}


enum class MainRoutes(private val label: String, val url: String) : ArbitraryRout {
    HOME("Home", "/"),
    VOIDED_TWEAKS("Voided Tweaks", "/voided-tweaks"),
    TRIMINATIOR("Triminator", "/triminator"),
    TEST("Test", "/test"),
    ADMIN("Admin", "/admin"),
    ERROR("Error", "/*");

    override fun label() = this.label
    override fun url() = this.url
}

interface ArbitraryRout {
    fun label(): String
    fun url(): String
}
