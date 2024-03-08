package org.teamvoided.data

import io.github.z4kn4fein.semver.Version

data class CategoryItem(
    val id: String,
    val categoryId: String,
    val name: String,
    val version: Version,
    val file: String,
    val minVer: Version,
    val maxVer: Version,
    val iconUrl: String = "https://vectorified.com/images/default-user-icon-33.jpg",
)

