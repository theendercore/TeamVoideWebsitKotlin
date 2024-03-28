package org.teamvoided.data

import io.github.z4kn4fein.semver.Version
import io.github.z4kn4fein.semver.toVersion
import org.teamvoided.database.Pack
import org.teamvoided.database.PackVersion
import org.teamvoided.util.DEFAULT_ICON
import java.net.URL

data class PackItem(
    val id: Short,
    val categoryId: Short,
    val name: String,
    val version: Version,
    val file: URL,
    val minVer: Version,
    val maxVer: Version,
    val iconUrl: String = DEFAULT_ICON,
) {
    constructor(pack: Pack, version: PackVersion) : this(
        pack.id,
        pack.category_id,
        pack.name,
        version.version.toVersion(),
        URL(version.url),
        version.min_version.toVersion(),
        version.max_version.toVersion()
    )

}

