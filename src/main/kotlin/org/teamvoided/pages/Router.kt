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
import org.teamvoided.util.assetFile
import org.teamvoided.util.respondBody
import org.teamvoided.util.respondCss

fun Application.Routing() = routing {

    staticResources("/static", "assets")

    get("/health-check") { call.respond(HttpStatusCode.OK) }

    get("/debug") { call.respond("Hello $this. ") }

    get("/") { call.respondHtml(HttpStatusCode.OK) { basicPage { home() } } }

    route("/voided-tweaks") {
        get { call.respondHtml { basicPage { voidedTweaks() } } }
        get("/data") { call.respondHtml { basicPage { voidedTweaks() } } }
        get("/crafting") { call.respondHtml { basicPage { voidedTweaks(VoidedTweaksRoutes.CRAFTING) } } }
        get("/resource") { call.respondHtml { basicPage { voidedTweaks(VoidedTweaksRoutes.RESOURCE) } } }
    }

    route("/triminator") { get { call.respondHtml { basicPage { triminator() } } } }

    get("/test") { call.respondHtml { basicPage { test() } } }

    route("/cmp") {

        route("/") { get { call.respondBody { home() } } }

        route("/voided-tweaks") {
            get { call.respondBody { voidedTweaks() } }
            get("/data") { call.respondBody { voidedTweaks() } }
            get("/crafting") { call.respondBody { voidedTweaks(VoidedTweaksRoutes.CRAFTING) } }
            get("/resource") { call.respondBody { voidedTweaks(VoidedTweaksRoutes.RESOURCE) } }
            route("{id}") {
                post("/packAdd") {
                    val id = call.parameters["id"]?.toShort()
                    call.respondBody { selectablePack(itemList.find { it.id == id }!!, Type.REMOVE) }
                }
                post("/packRemove") {
                    val id = call.parameters["id"]?.toShort()
                    call.respondBody { selectablePack(itemList.find { it.id == id }!!, Type.ADD) }
                }
            }

        }

        get("/test") { call.respondBody { test() } }

        get("/triminator") { call.respondBody { triminator() } }

        get("/*") { call.respondBody { errorPage() } }
    }

    get("/*") { call.respondHtml { basicPage { errorPage() } } }

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
    subnav(VoidedTweaksRoutes.entries)
    div {
        h1 {
            when (page) {
                VoidedTweaksRoutes.DATA -> voidedTweaksTemplate()
                VoidedTweaksRoutes.CRAFTING -> +"Hello CRAFTING"
                VoidedTweaksRoutes.RESOURCE -> +"Hello from RESOURCE"
            }
        }
    }
}

fun FlowContent.test() {
    makeTestPage()
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
