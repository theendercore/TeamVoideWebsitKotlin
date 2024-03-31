package org.teamvoided.pages

import arrow.core.getOrElse
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import org.teamvoided.data.CategoryItem
import org.teamvoided.data.CategoryType
import org.teamvoided.data.PackItem
import org.teamvoided.env.Dependencies
import org.teamvoided.util.Version
import java.net.URL


fun Route.voidedTweaksRout(module: Dependencies) {
    get("/debug") {
        val x = module.voidedTweaksService.getCategoriesByType(CategoryType.DATA)

        call.respond("This is data: ${x.getOrElse { it }}")
    }
}

fun testItem(categoryId: Short, name: String) = PackItem(
    2, categoryId,
    name,
    Version("1.0.0"),
    URL("http://0.0.0.0:8080/"),
    Version("1.20.2"),
    Version("1.2.5")
)

fun testCat(id: Short, name: String, packs: List<PackItem>) = CategoryItem(id, name, CategoryType.DATA, packs)
const val MAIN_PREFIX = "/cmp/voided-tweaks"

@Suppress("MagicNumber")
val itemList = listOf(
    testItem(4435, "Terra Ore"),
    testItem(4435, "Orange Planks"),
    testItem(4435, "Redstone House"),
    testItem(5454, "Hell Ore Processor"),
    testItem(5454, "Hell Planks"),
    testItem(5454, "Hallowwer")
)


@Suppress("MagicNumber")
val categoryList = listOf(
    testCat(4435, "Ola", itemList.filter { it.categoryId == 4435.toShort() }),
    testCat(5454, "Hell", itemList.filter { it.categoryId == 5454.toShort() })
)


fun FlowContent.voidedTweaksPage(module: Dependencies, page: VoidedTweaksRoutes = VoidedTweaksRoutes.DATA) {
    subNav(VoidedTweaksRoutes.entries)
    div {
        packCraftinator(module, page)
    }
}

fun FlowContent.packCraftinator(module: Dependencies, packRout: VoidedTweaksRoutes) {
    val ignored = module.voidedTweaksService
    if (packRout == VoidedTweaksRoutes.DATA) {
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
                categoryList.filter { isTheSameAsRout(it.type, packRout) }.forEach { category ->
                    div("flex flex-col gap-2 px-8 py-3 bg-white bg-opacity-5 rounded-3xl ") {
                        h1("pl-5 text-3xl font-bold font-mono") { +category.name }
                        div("flex flex-wrap gap-2") { category.packs.forEach { selectablePack(it) } }
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
    } else div { +packRout.name }
}

fun isTheSameAsRout(type: CategoryType, packRout: VoidedTweaksRoutes): Boolean {
    return packRout == (when (type) {
        CategoryType.DATA -> VoidedTweaksRoutes.DATA
        CategoryType.CRAFTING -> VoidedTweaksRoutes.CRAFTING
        CategoryType.RESOURCE -> VoidedTweaksRoutes.RESOURCE
    })
}

fun FlowContent.selectablePack(item: PackItem, type: Type = Type.ADD) {
    button(
        classes = "w-28 lg:w-32 text-center flex flex-col items-center rounded-xl bg-opacity-5 px-3 py-6 cursor-pointer " +
                if (type == Type.REMOVE) "bg-white" else ""
    ) {
        attributes["hx-post"] = "$MAIN_PREFIX/${item.id}/pack${type.id}"
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
enum class VoidedTweaksRoutes(private val label: String, val url: String) : ArbitraryRout {
    DATA("Data Packs", "/data"),
    CRAFTING("Crafting Tweaks", "/crafting"),
    RESOURCE("Resource Packs", "/resource");

    override fun label() = this.label
    override fun url() = "/voided-tweaks" + this.url
}
