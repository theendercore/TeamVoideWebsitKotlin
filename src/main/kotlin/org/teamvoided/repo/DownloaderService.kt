package org.teamvoided.repo

import arrow.core.Either
import arrow.core.raise.either
import org.teamvoided.DomainError
import org.teamvoided.data.CategoryType

interface DownloaderService {
    fun downloadPacks(packIds: List<Short>, categoryType: CategoryType): Either<DomainError, String>

}

@Suppress("UnusedParameter")
fun downloaderService(
    packPersistence: PackPersistence,
    versionPersistence: VersionPersistence
): DownloaderService {
    return object : DownloaderService {
        override fun downloadPacks(packIds: List<Short>, categoryType: CategoryType): Either<DomainError, String> =
            either {
                "$categoryType:" +
                        packIds
                            .map { packPersistence.getById(it) }
                            .bindAll()
                            .joinToString { "${it.name}(${it.description})" }
            }

    }
}
