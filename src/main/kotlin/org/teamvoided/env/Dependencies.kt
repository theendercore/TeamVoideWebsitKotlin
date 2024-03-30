package org.teamvoided.env

import arrow.fx.coroutines.ResourceScope
import org.teamvoided.repo.*

class Dependencies(
    val voidedTweaksService: VoidedTweaksService,
    val creatorService: CreatorService
)

suspend fun ResourceScope.dependencies(env: Env): Dependencies {
    val hikari = hikari(env.dataSource)
    val sqlDelight = sqlDelight(hikari)

    val packPersistence = packPersistence(sqlDelight.packQueries)
    val categoryPersistence = categoryPersistence(sqlDelight.categoryQueries)

    val creatorService = creatorService(packPersistence, categoryPersistence)
    val voidedTweaksService = voidedTweaksService(packPersistence, categoryPersistence)

    return Dependencies(voidedTweaksService, creatorService)
}
