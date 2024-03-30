package org.teamvoided.pages

import arrow.core.Either
import arrow.core.getOrElse
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.css.p
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
                val x = call.receiveText()
                val q = call.request.rawQueryParameters.toMap().map { (a, b) -> b + a }.toString()
                call.respondBody { p { +"$x -$q" } }
            }
        }

    }

    get("/debug") {
        val x = module.voidedTweaksService.getCategories(CategoryType.DATA)

        call.respond("This is data: ${x.getOrElse { it }}")
    }
}

fun FlowContent.adminPanelPage(dependencies: Dependencies) {
    val categories = dependencies.voidedTweaksService.getAllCategories()
    div("pt-8 w-full flex flex-col gap-6 items-center justify-center px-20") {
        h1("text-3xl black font-mono") { +"The super secret and evil admin panel!" }
        div("w-full flex gap-8 p-2 justify-around") {
            form(classes = "flex flex-col gap-4 p-4 items-center justify-center") {
                attributes["hx-put"] = "/cmp/admin/categories"
                attributes["hx-target"] = "#category-content"
                attributes["hx-swap"] = "innerHTML"
                h3("text-lg bold") { +"Categories Form" }
                div("w-full flex gap-2 items-center justify-between") {
                    label { +"Name " }
                    input {
                        type = InputType.text
                        name = "name"
                    }
                }
                div("w-full flex gap-2 items-center justify-between") {
                    label { +"Type" }
                    input {
                        type = InputType.text
                        name = "type"
                    }
                }
                button(classes = "px-6 py-2 bg-white bg-opacity-20 rounded-full") { +"Submit" }
            }

            div("flex flex-col gap-2") {
                attributes["id"] = "category-content"
                when (categories) {
                    is Either.Left -> div("flex flex-col items-center p-4") {
                        h3("text-lg bold") { +"No Categories found!" }
                        p("italic opacity-80") { +categories.value.toString() }
                    }

                    is Either.Right -> categories.value.forEach { div("p-4") { +it.name } }
                }
            }
        }
    }
}
