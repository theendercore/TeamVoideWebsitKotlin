package org.teamvoided.pages

import arrow.core.Either
import arrow.core.getOrElse
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import org.teamvoided.data.CategoryType
import org.teamvoided.data.PackItem
import org.teamvoided.env.Dependencies
import org.teamvoided.util.format
import org.teamvoided.util.respondBody


const val CMP_PREFIX = "/cmp/voided-tweaks"
fun Route.voidedTweaksRout(module: Dependencies) {

    route("/voided-tweaks") {
        get { call.respondHtml { htmlWrapper { voidedTweaksPage(module) } } }
        get("/data") { call.respondHtml { htmlWrapper { voidedTweaksPage(module) } } }
        get("/crafting") { call.respondHtml { htmlWrapper { voidedTweaksPage(module, VoidedTweaksRoutes.CRAFTING) } } }
        get("/resource") { call.respondHtml { htmlWrapper { voidedTweaksPage(module, VoidedTweaksRoutes.RESOURCE) } } }
        get("/debug") {
            val x = module.voidedTweaksService.getCategoriesByTypeOld(CategoryType.DATA)

            call.respond("This is data: ${x.getOrElse { it }}")
        }
    }
    route(CMP_PREFIX) {
        get { call.respondBody { voidedTweaksPage(module) } }
        get("/data") { call.respondBody { voidedTweaksPage(module) } }
        get("/crafting") { call.respondBody { voidedTweaksPage(module, VoidedTweaksRoutes.CRAFTING) } }
        get("/resource") { call.respondBody { voidedTweaksPage(module, VoidedTweaksRoutes.RESOURCE) } }
        route("{id}") {
            post("/pack-add") {
                when (val id = call.parameters["id"]?.toShort()) {
                    null -> call.respondBody { }
                    else -> when (val pack = module.voidedTweaksService.getPackById(id)) {
                        is Either.Left -> call.respondBody { }
                        is Either.Right -> call.respondBody { selectablePack(pack.value, Type.REMOVE) }
                    }
                }
            }
            post("/pack-remove") {
                when (val id = call.parameters["id"]?.toShort()) {
                    null -> call.respondBody { }
                    else -> when (val pack = module.voidedTweaksService.getPackById(id)) {
                        is Either.Left -> call.respondBody { }
                        is Either.Right -> call.respondBody { selectablePack(pack.value, Type.ADD) }
                    }
                }
            }
        }

    }
}


fun FlowContent.voidedTweaksPage(module: Dependencies, page: VoidedTweaksRoutes = VoidedTweaksRoutes.DATA) {
    subNav(VoidedTweaksRoutes.entries)
    div {
        packCraftinator(module, getCategoryType(page))
    }
}

fun FlowContent.packCraftinator(module: Dependencies, category: CategoryType) {
    val catgories = module.voidedTweaksService.getCategoriesByType(category)
    div("flex gap-6 p-8") {
        div("p-6 flex flex-col gap-4 bg-white bg-opacity-5 w-full rounded-3xl") {
            div("p-4 px-6 flex bg-white bg-opacity-10 rounded-3xl justify-between") {
                h1("text-xl font-semibold") { +"Minecraft version" }
                div("flex gap-1 text-center") {
                    button(classes = "font-bold text-xl ") { +"<" }
                    button(classes = "cursor-pointer hover:underline p-1 px-.5") { +"1.18" }
                    button(classes = "cursor-pointer hover:underline p-1 px-.5") { +"1.19" }
                    button(classes = "cursor-pointer hover:underline text-xl font-bold underline pb-2 pt-1") { +"1.20" }
                    button(classes = "font-bold text-xl") { +">" }
                }
            }
            div {
                when (catgories) {
                    is Either.Left -> +catgories.value.toString()
                    is Either.Right -> catgories.value.forEach { category ->
                        div("flex flex-col gap-2 px-8 py-3 bg-white bg-opacity-5 rounded-3xl ") {
                            h1("pl-5 text-3xl font-bold font-mono") { +category.name }
                            div("flex flex-wrap gap-2") { category.packs.forEach { selectablePack(it) } }
                        }
                    }
                }
            }

        }
        div("flex flex-col w-64 gap-5 p-5") {
            div("flex flex-col gap-2 bg-white bg-opacity-10 p-3 rounded-xl") {
                span("text-lg") { +"Selected Packs:" }
                ul("flex flex-col gap-2 bg-bg rounded p-2") { id = "pack-list" }
            }
            button(classes = "bg-white bg-opacity-5 py-3 text-xl semibold rounded-xl") { +"Download" }
        }
    }
}

fun getCategoryType(packRout: VoidedTweaksRoutes): CategoryType {
    return when (packRout) {
        VoidedTweaksRoutes.DATA -> CategoryType.DATA
        VoidedTweaksRoutes.CRAFTING -> CategoryType.CRAFTING
        VoidedTweaksRoutes.RESOURCE -> CategoryType.RESOURCE
    }
}

fun FlowContent.selectablePack(pack: PackItem, type: Type = Type.ADD) {
    when (type) {
        Type.ADD -> div { attributes["hx-swap-oob"] = "delete:#${format(pack.name)}" }
        Type.REMOVE ->
            ul {
                attributes["hx-swap-oob"] = "beforeend:#pack-list"
                li("px-2") {
                    attributes["data-id"] = pack.id.toString()
                    id = format(pack.name)
                    +"> ${pack.name}"
                }
            }
    }

    button(
        classes = "w-28 lg:w-32 text-center flex flex-col items-center rounded-xl bg-opacity-5 px-3 py-6 cursor-pointer " +
                if (type == Type.REMOVE) "bg-white" else ""
    ) {
        attributes["hx-post"] = "$CMP_PREFIX/${pack.id}/pack-${type.id}"
        attributes["hx-swap"] = "outerHTML"
        attributes["data-id"] = pack.id.toString()
        id = pack.id.toString()
        img("$pack icon", classes = "w-16") {
            src = "https://vectorified.com/images/default-user-icon-33.jpg"
        }
        h3("font-semibold text-lg") { +pack.name }
        span("italic text-sm opacity-50") { +pack.version.toString() }
    }
}

enum class Type(val id: String) { ADD("add"), REMOVE("remove"); }
enum class VoidedTweaksRoutes(private val label: String, val url: String) : ArbitraryRout {
    DATA("Data Packs", "/data"),
    CRAFTING("Crafting Tweaks", "/crafting"),
    RESOURCE("Resource Packs", "/resource");

    override fun label() = this.label
    override fun url() = "/voided-tweaks" + this.url
}
