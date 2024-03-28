package org.teamvoided.pages

import kotlinx.html.*
import org.teamvoided.data.PackItem
import org.teamvoided.util.Version
import java.net.URL

fun testItem(categoryId: Short, name: String) = PackItem(
    2, categoryId,
    name,
    Version("1.0.0"),
    URL("yes"),
    Version("1.20.2"),
    Version("1.2.5")
)


val mainPrefix = "/cmp/voided-tweaks"
val itemList = listOf(
    testItem(4435, "Terra Ore"),
    testItem(4435, "Orange Planks"),
    testItem(4435, "Redstone House"),
    testItem(5454, "Hell Ore Prcoessor"),
    testItem(5454, "Hell Planks"),
    testItem(5454, "Hallowwer")
)

val categoryList = mapOf(
    "Ola" to itemList.filter { it.categoryId == 4435.toShort() },
    "Hell" to itemList.filter { it.categoryId == 5454.toShort() }
)

fun FlowContent.voidedTweaksTemplate() {
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
            categoryList.forEach { item ->
                div("flex flex-col gap-2 px-8 py-3 bg-white bg-opacity-5 rounded-3xl ") {
                    h1("pl-5 text-3xl font-bold font-mono") { +item.key }
                    div("flex flex-wrap gap-2") { item.value.forEach { selectablePack(it, Type.ADD) } }
                }
            }

        }
        div("flex flex-col w-64 gap-5 p-5") {
            div("flex flex-col gap-2 bg-white bg-opacity-10 p-3 rounded-xl") {
                span("text-lg") { +"Selected Packs:" }
                ul("bg-bg rounded") {
                    li("p-1") { +"Orange Planks" }
                }
            }
            button(classes = "bg-white bg-opacity-5 py-3 text-xl semibold rounded-xl") { +"Download" }
        }
    }
}

fun FlowContent.selectablePack(item: PackItem, type: Type) {
    button(
        classes = "w-28 lg:w-32 text-center flex flex-col items-center rounded-xl bg-opacity-5 px-3 py-6 cursor-pointer " +
                if (type == Type.REMOVE) "bg-white" else ""
    ) {
        attributes["hx-post"] = "$mainPrefix/${item.id}/pack${type.id}"
        attributes["hx-swap"] = "outerHTML"
        id = item.id.toString()
        img("$item icon", classes = "w-16") {
            src = "https://vectorified.com/images/default-user-icon-33.jpg"
        }
        h3("font-semibold text-lg") { +item.name }
        span("italic text-sm opacity-50") { +item.version.toString() }
    }
}

enum class Type(val id: String) { ADD("Add"), REMOVE("Remove"); }