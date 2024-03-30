package org.teamvoided.env

import arrow.fx.coroutines.ResourceScope
import org.teamvoided.repo.VoidedTweaksService
import org.teamvoided.repo.packPersistence
import org.teamvoided.repo.voidedTweaksService

class Dependencies(voidedTweaksService: VoidedTweaksService)

suspend fun ResourceScope.dependencies(env: Env): Dependencies {
    val hikari = hikari(env.dataSource)
    val sqlDelight = sqlDelight(hikari)
    val packPersistence = packPersistence(sqlDelight.packQueries)
    val voidedTweaksService = voidedTweaksService(packPersistence)

    return Dependencies(voidedTweaksService)
}
