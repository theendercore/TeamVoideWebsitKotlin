package org.teamvoided.repo

import arrow.core.Either
import arrow.core.raise.either
import org.teamvoided.DomainError
import org.teamvoided.data.CategoryType
import java.net.URL

interface CreatorService {
    fun addCategory(name: String, type: CategoryType): Either<DomainError, Short>
    fun deleteCategory(id: Short): DomainError?

    fun addPack(category: Short, name: String, description: String, icon: URL): Either<DomainError, Short>
    fun deletePack(id: Short): DomainError?
    fun addVersion(
        packId: Short, minVersion: String, maxVersion: String, url: URL, version: String
    ): Either<DomainError, Short>

    fun deleteVersion(id: Short): DomainError?
}

fun creatorService(
    categoryPersistence: CategoryPersistence,
    packPersistence: PackPersistence,
    versionPersistence: VersionPersistence
): CreatorService {
    return object : CreatorService {
        override fun addCategory(name: String, type: CategoryType): Either<DomainError, Short> = either {
            categoryPersistence.create(name, type).bind()
        }

        override fun deleteCategory(id: Short): DomainError? = categoryPersistence.deleteById(id).leftOrNull()


        override fun addPack(
            category: Short, name: String, description: String, icon: URL
        ): Either<DomainError, Short> =
            either { packPersistence.create(category, name, description, icon).bind() }

        override fun deletePack(id: Short): DomainError? = packPersistence.deleteById(id).leftOrNull()


        override fun addVersion(
            packId: Short, minVersion: String, maxVersion: String, url: URL, version: String
        ): Either<DomainError, Short> =
            either { versionPersistence.create(packId, minVersion, maxVersion, url, version).bind() }

        override fun deleteVersion(id: Short): DomainError? = versionPersistence.deleteById(id).leftOrNull()
    }
}
