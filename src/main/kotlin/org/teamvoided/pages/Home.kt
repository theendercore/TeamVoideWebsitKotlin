package org.teamvoided.pages

import kotlinx.html.*
import org.teamvoided.util.assetFile


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


