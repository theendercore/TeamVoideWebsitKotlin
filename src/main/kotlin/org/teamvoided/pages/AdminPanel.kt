package org.teamvoided.pages

import arrow.core.Either
import arrow.core.getOrElse
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import org.teamvoided.data.CategoryType
import org.teamvoided.env.Dependencies
import org.teamvoided.util.respondBody
import kotlin.collections.set

fun Route.adminPanelRout(module: Dependencies) {

    get("/admin") { call.respondHtml { htmlWrapper { adminPanelPage(module) } } }

    route("/cmp") {
        route("/admin") {

            get("") { call.respondBody { adminPanelPage(module) } }

            put("/categories") {
                val params = call.receiveParameters()

                module.creatorService.addCategory(params["name"]!!, CategoryType.valueOf(params["type"]!!))
                call.respondBody { categoryList(module) }
            }
            delete("/categories") {
                val params = call.receiveParameters()
                module.creatorService.deleteCategory(params["id"]!!.toShort())
//                module.creatorService.addCategory(params["name"]!!, CategoryType.valueOf(params["type"]!!))
                call.respondBody { categoryList(module) }
            }
        }

    }

    get("/debug") {
        val x = module.voidedTweaksService.getCategories(CategoryType.DATA)

        call.respond("This is data: ${x.getOrElse { it }}")
    }
}

fun FlowContent.adminPanelPage(module: Dependencies) {
    div("pt-8 w-full flex flex-col gap-6 items-center justify-center px-20") {
        h1("text-3xl black font-mono") { +"The super secret and evil admin panel!" }
        div("w-full flex gap-8 p-2 justify-around") {
            form(classes = "flex flex-col gap-4 p-4 items-center justify-center") {
                attributes["hx-put"] = "/cmp/admin/categories"
                attributes["hx-target"] = "#category-content"
                attributes["hx-swap"] = "innerHTML"
                h3("text-lg bold") { +"Categories Form" }
                div("w-full flex gap-4 items-center justify-between") {
                    label { +"Name " }
                    input(classes = "max-w-56 px-2 py-1 bg-neutral-800 rounded-lg focus:outline-none") {
                        type = InputType.text
                        name = "name"
                    }
                }
                div("w-full flex gap-4 items-center justify-between") {
                    label { +"Type" }
//                    input(classes = "px-2 py-1 bg-neutral-800 rounded-lg focus:outline-none") {
//                        type = InputType.text
//                        name = "type"
//                    }
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
