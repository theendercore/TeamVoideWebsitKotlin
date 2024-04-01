package org.teamvoided.pages

import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.html.*
import org.teamvoided.env.Dependencies
import org.teamvoided.util.respondBody


fun Route.testRouting(ignored: Dependencies) {
    route("/cmp") {
        get("/test") { call.respondBody { testPage() } }
    }

    route("/test") {
        get() { call.respondHtml { htmlWrapper { testPage() } } }

        get("/debug") {
            val z  = call.parameters["data-id"]!!
            call.respondBody { +"$z" }
        }
    }
}

@Suppress("LongMethod")
fun FlowContent.testPage() {
    div("flex items-center justify-center") {

        button {
            attributes["hx-get"] = "/test/debug"
            attributes["hx-on:htmx:configRequest"] = "event.detail.parameters.elementId = this.id"
            attributes["hx-swap"] = "none"
            id = "hello_there"
            +"Click Me"
        }
    }





    section("w-full py-6") {
        div("container grid items-center justify-center gap-4 px-4 text-center md:px-6") {
            div("space-y-3") {
                h2("text-3xl font-bold tracking-tighter sm:text-4xl md:text-5xl") { +"""Interactive Widgets""" }
                p("max-w-[700px] text-gray-500 md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed dark:text-gray-400") { +"""Beautifully designed interactive elements to enhance your app.""" }
            }
            div("grid max-w-sm gap-4 mx-auto items-start sm:max-w-2xl sm:grid-cols-2 lg:max-w-none lg:grid-cols-4") {
                div("flex flex-col gap-1") {
                    button(classes = "inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 border border-input bg-background hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2 w-full") { +"""Button""" }
                    button(classes = "inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 border border-input bg-background hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2 w-full") { +"""Button""" }
                }
                div("flex flex-col gap-1") {
                    button(classes = "inline-flex items-center justify-center rounded-md text-sm font-medium ring-offset-background transition-colors hover:bg-muted hover:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 data-[state=on]:bg-accent data-[state=on]:text-accent-foreground bg-transparent h-10 px-3 w-full") {
                        type = ButtonType.button
                        attributes["aria-pressed"] = "false"
                        attributes["data-state"] = "off"
                        id = "toggle1"
                    }
                    label("sr-only") {
                        htmlFor = "toggle1"
                        +"""Toggle"""
                    }
                    button(classes = "inline-flex items-center justify-center rounded-md text-sm font-medium ring-offset-background transition-colors hover:bg-muted hover:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 data-[state=on]:bg-accent data-[state=on]:text-accent-foreground bg-transparent h-10 px-3 w-full") {
                        type = ButtonType.button
                        attributes["aria-pressed"] = "false"
                        attributes["data-state"] = "off"
                        id = "toggle2"
                    }
                    label("sr-only") {
                        htmlFor = "toggle2"
                        +"""Toggle"""
                    }
                }
                div("flex flex-col gap-1") {
                    span("relative flex touch-none select-none items-center w-full") {
                        dir = Dir.ltr
                        attributes["data-orientation"] = "horizontal"
                        attributes["aria-disabled"] = "false"
                        id = "slider1"
                        style = "--radix-slider-thumb-transform: translateX(-50%);"
                        span("relative h-2 w-full grow overflow-hidden rounded-full bg-secondary") {
                            attributes["data-orientation"] = "horizontal"
                            span("absolute h-full bg-primary") {
                                attributes["data-orientation"] = "horizontal"
                                style = "left: 0%; right: 100%;"
                            }
                        }
                        span {
                            style =
                                "transform: var(--radix-slider-thumb-transform); position: absolute; left: calc(0% + 10px);"
                            span("block h-5 w-5 rounded-full border-2 border-primary bg-background ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50") {
                                role = "slider"
                                attributes["aria-valuemin"] = "0"
                                attributes["aria-valuemax"] = "100"
                                attributes["aria-orientation"] = "horizontal"
                                attributes["data-orientation"] = "horizontal"
//                                tabindex = "0"
                                style = ""
                                attributes["data-radix-collection-item"] = ""
                                attributes["aria-valuenow"] = "0"
                            }
                        }
                    }
                    label("sr-only") {
                        htmlFor = "slider1"
                        +"""Slider"""
                    }
                    span("relative flex touch-none select-none items-center w-full") {
                        dir = Dir.ltr
                        attributes["data-orientation"] = "horizontal"
                        attributes["aria-disabled"] = "false"
                        id = "slider2"
                        style = "--radix-slider-thumb-transform: translateX(-50%);"
                        span("relative h-2 w-full grow overflow-hidden rounded-full bg-secondary") {
                            attributes["data-orientation"] = "horizontal"
                            span("absolute h-full bg-primary") {
                                attributes["data-orientation"] = "horizontal"
                                style = "left: 0%; right: 50%;"
                            }
                        }
                        span {
                            style =
                                "transform: var(--radix-slider-thumb-transform); position: absolute; left: calc(50% + 0px);"
                            span("block h-5 w-5 rounded-full border-2 border-primary bg-background ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50") {
                                role = "slider"
                                attributes["aria-valuemin"] = "0"
                                attributes["aria-valuemax"] = "100"
                                attributes["aria-orientation"] = "horizontal"
                                attributes["data-orientation"] = "horizontal"
//                                tabindex = "0"
                                style = ""
                                attributes["data-radix-collection-item"] = ""
                                attributes["aria-valuenow"] = "50"
                            }
                        }
                    }
                    label("sr-only") {
                        htmlFor = "slider2"
                        +"""Slider"""
                    }
                }
                div("flex flex-col gap-1") {
                    div("w-full") {
                    }
                    label("sr-only") {
                        htmlFor = "knob1"
                        +"""Knob"""
                    }
                    div("w-full") {
                    }
                    label("sr-only") {
                        htmlFor = "knob2"
                        +"""Knob"""
                    }
                }
            }
        }
    }
}
