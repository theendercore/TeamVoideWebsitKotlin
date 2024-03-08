package org.teamvoided.util

import kotlin.io.path.Path

fun assetFile(path:String) = Path("static/$path").toString()