package org.teamvoided.pages

import kotlinx.html.*

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

fun FlowContent.subNav(
    tabs: List<ArbitraryRout>,
    header: String = "w-full px-20 flex-col flex",
    nav: String = "w-full flex items-center justify-center gap-10 text-lg",
    a: String = "hover:underline hover:scale-110"
) = nav(tabs, header, nav, a)
