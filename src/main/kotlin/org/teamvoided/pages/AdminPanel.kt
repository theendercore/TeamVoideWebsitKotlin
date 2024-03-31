package org.teamvoided.pages

import arrow.core.Either
import arrow.core.getOrElse
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import kotlinx.serialization.Serializable
import org.teamvoided.data.CategoryType
import org.teamvoided.env.Dependencies
import org.teamvoided.util.DEFAULT_ICON
import org.teamvoided.util.respondBody
import java.net.URL
import kotlin.collections.set

fun Route.adminPanelRout(module: Dependencies) {

    get("/admin") { call.respondHtml { htmlWrapper { adminPanelPage(module) } } }

    route("/cmp") {
        route("/admin") {

            get("") { call.respondBody { adminPanelPage(module) } }
            route("/categories") {
                put {
                    val params = call.receiveParameters()
                    module.creatorService.addCategory(params["name"]!!.trim(), CategoryType.valueOf(params["type"]!!))
                    call.respondBody { categoryList(module) }
                }
                delete {
                    val params = call.receiveParameters()
                    module.creatorService.deleteCategory(params["id"]!!.toShort())
                    call.respondBody { categoryList(module) }
                }
            }
            route("/packs") {
                put {
                    val params = call.receiveParameters()
                    module.creatorService.addPack(
                        params["category_id"]!!.toShort(), params["name"]!!.trim(),
                        params["description"]!!, URL(DEFAULT_ICON)
                    )
                    call.respondBody { packList(module) }
                }
                delete {
                    val params = call.receiveParameters()
                    module.creatorService.deletePack(params["id"]!!.toShort())
                    call.respondBody { packList(module) }
                }
            }
            route("/versions") {
                put {
                    val params = call.receiveParameters()
                    module.creatorService.addVersion(
                        params["pack_id"]!!.toShort(),
                        params["min_version"]!!,
                        params["max_version"]!!,
                        URL(params["url"]!!),
                        params["version"]!!
                    )
                    call.respondBody { versionList(module) }
                }
                delete {
                    val params = call.receiveParameters()
                    module.creatorService.deleteVersion(params["id"]!!.toShort())
                    call.respondBody { versionList(module) }
                }
            }
        }

    }

    get("/api/packs") {
        val x = module.voidedTweaksService.getAllPacks()
            .map { v -> v.map { Pack2(it.id, it.category_id, it.name, it.description, it.icon) } }

        call.respond(x.getOrElse { it })
    }
}

@Serializable
data class Pack2(val id: Short, val categoryId: Short, val name: String, val description: String, val icon: String)

fun FlowContent.adminPanelPage(module: Dependencies) {
    div("pt-8 w-full flex flex-col gap-6 items-center justify-center px-20") {
        h1("text-3xl black font-mono") { +"The super secret and evil admin panel!" }
        div("w-full flex gap-8 p-2 justify-around") {
            form(classes = "flex flex-col gap-4 p-4 items-center justify-center") {
                attributes["hx-put"] = "/cmp/admin/categories"
                attributes["hx-target"] = "#category-content"
                attributes["hx-swap"] = "innerHTML"
                h3("text-lg bold") { +"Categories Form" }
                styledInput("Name ", "name")
                div("w-full flex gap-4 items-center justify-between") {
                    label { +"Type" }
                    select("w-full max-w-56 px-3 py-2 bg-neutral-800 rounded-lg focus:outline-none") {
                        name = "type"
                        CategoryType.entries.forEach {
                            option {
                                value = it.name
                                +it.name
                            }
                        }
                    }
                }
                button(classes = "px-6 py-2 bg-white bg-opacity-20 rounded-full") { +"Submit" }
            }

            div("flex flex-col gap-2") {
                attributes["id"] = "category-content"
                categoryList(module)
            }
        }
        div("w-full flex gap-8 p-2 justify-around") {
            when (val categories = module.voidedTweaksService.getAllCategories()) {
                is Either.Left -> div("flex flex-col items-center p-4") {
                    h3("text-lg bold") { +"Make Categories some to add Packs!" }
                }

                is Either.Right -> {
                    form(classes = "flex flex-col gap-4 p-4 items-center justify-center") {
                        attributes["hx-put"] = "/cmp/admin/packs"
                        attributes["hx-target"] = "#pack-content"
                        attributes["hx-swap"] = "innerHTML"
                        h3("text-lg bold") { +"Pack Form" }
                        styledInput("Name ", "name")
                        styledInput("Description ", "description")
                        styledInput("Icon ", "icon")

                        div("w-full flex gap-4 items-center justify-between") {
                            label { +"Category" }
                            select("w-full max-w-56 px-3 py-2 bg-neutral-800 rounded-lg focus:outline-none") {
                                name = "category_id"
                                required = true
                                categories.value.forEach {
                                    option {
                                        value = it.id.toString()
                                        +it.name
                                    }
                                }
                            }
                        }
                        button(classes = "px-6 py-2 bg-white bg-opacity-20 rounded-full") { +"Submit" }
                    }
                }
            }

            div("flex flex-col gap-2") {
                attributes["id"] = "pack-content"
                packList(module)
            }
        }

        div("w-full flex gap-8 p-2 justify-around") {
            when (val packs = module.voidedTweaksService.getAllPacks()) {
                is Either.Left -> div("flex flex-col items-center p-4") {
                    h3("text-lg bold") { +"Make some Packs to add Versions!" }
                }

                is Either.Right -> {
                    form(classes = "flex flex-col gap-4 p-4 items-center justify-center") {
                        attributes["hx-put"] = "/cmp/admin/versions"
                        attributes["hx-target"] = "#version-content"
                        attributes["hx-swap"] = "innerHTML"
                        h3("text-lg bold") { +"Version Form" }
                        div("w-full flex gap-4 items-center justify-between") {
                            label { +"Pack" }
                            select("w-full max-w-56 px-3 py-2 bg-neutral-800 rounded-lg focus:outline-none") {
                                name = "pack_id"
                                required = true
                                packs.value.forEach {
                                    option {
                                        value = it.id.toString()
                                        +it.name
                                    }
                                }
                            }
                        }

                        styledInput("Link ", "url", InputType.url)
                        styledInput("Version (as in Pack Version) ", "version")
                        styledInput("Min Version (Minecraft) ", "min_version")
                        styledInput("Max Version (Minecraft) ", "max_version")

                        button(classes = "px-6 py-2 bg-white bg-opacity-20 rounded-full") { +"Submit" }
                    }
                }
            }

            div("flex flex-col gap-2") {
                attributes["id"] = "version-content"
                versionList(module)
            }
        }
    }
}

fun FlowContent.categoryList(module: Dependencies) {
    when (val categories = module.voidedTweaksService.getAllCategories()) {
        is Either.Left -> div("flex flex-col items-center p-4") {
            h3("text-lg bold") { +"No Categories found!" }
            p("italic opacity-80") { +categories.value.toString() }
        }

        is Either.Right -> categories.value.forEach {
            div("flex gap-2 items-center justify-center p-1") {
                span {
                    +it.name
                    span("inline opacity-80") { +" (${it.type})" }
                }
                form {
                    attributes["hx-delete"] = "/cmp/admin/categories"
                    attributes["hx-target"] = "#category-content"
                    attributes["hx-swap"] = "innerHTML"
                    input(classes = "hidden") {
                        name = "id"
                        value = it.id.toString()
                    }
                    button(classes = "px-3 py-1 rounded-full bg-slate-800") { +" x " }
                }
            }
        }
    }
}

fun FlowContent.packList(module: Dependencies) {
    when (val packs = module.voidedTweaksService.getAllPacks()) {
        is Either.Left -> div("flex flex-col items-center p-4") {
            h3("text-lg bold") { +"No Packs found!" }
            p("italic opacity-80") { +packs.value.toString() }
        }

        is Either.Right -> packs.value.forEach {
            div("flex gap-2 items-center justify-center p-1") {
                span {
                    +it.name
                    span("inline opacity-80 text-sm") { +" (${it.description})" }
                }
                form {
                    attributes["hx-delete"] = "/cmp/admin/packs"
                    attributes["hx-target"] = "#pack-content"
                    attributes["hx-swap"] = "innerHTML"
                    input(classes = "hidden") {
                        name = "id"
                        value = it.id.toString()
                    }
                    button(classes = "px-3 py-1 rounded-full bg-slate-800") { +" x " }
                }
            }
        }
    }
}

fun FlowContent.versionList(module: Dependencies) {
    when (val versions = module.voidedTweaksService.getAllVersions()) {
        is Either.Left -> div("flex flex-col items-center p-4") {
            h3("text-lg bold") { +"No Versions found!" }
            p("italic opacity-80") { +versions.value.toString() }
        }

        is Either.Right -> versions.value.forEach {
            div("flex gap-2 items-center justify-center p-1") {
                span {
                    +it.version
                    span("inline opacity-80") { +" (${it.pack_id})" }
                }
                form {
                    attributes["hx-delete"] = "/cmp/admin/versions"
                    attributes["hx-target"] = "#version-content"
                    attributes["hx-swap"] = "innerHTML"
                    input(classes = "hidden") {
                        name = "id"
                        value = it.id.toString()
                    }
                    button(classes = "px-3 py-1 rounded-full bg-slate-800") { +" x " }
                }
            }
        }
    }
}

fun FlowContent.styledInput(
    label: String,
    nameIn: String,
    inputType: InputType = InputType.text,
    isRequired: Boolean = true
) =
    div("w-full flex gap-4 items-center justify-between") {
        label { +label }
        input(classes = "max-w-56 px-2 py-1 bg-neutral-800 rounded-lg focus:outline-none") {
            type = inputType
            name = nameIn
            required = isRequired
        }
    }

