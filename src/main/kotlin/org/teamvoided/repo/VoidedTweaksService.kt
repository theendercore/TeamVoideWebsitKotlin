package org.teamvoided.repo

import arrow.core.Either
import arrow.core.raise.either
import org.teamvoided.DomainError
import org.teamvoided.data.CategoryType
import org.teamvoided.database.Category
import org.teamvoided.database.Pack
import org.teamvoided.database.PackVersion

interface VoidedTweaksService {
    fun getCategoriesByType(type: CategoryType): Either<DomainError, List<Category>>
    fun getAllCategories(): Either<DomainError, List<Category>>

    fun getPacksByCategory(categoryId: Short): Either<DomainError, List<Pack>>
    fun getAllPacks(): Either<DomainError, List<Pack>>

    fun getVersionsByPack(packId: Short): Either<DomainError, List<PackVersion>>
    fun getAllVersions(): Either<DomainError, List<PackVersion>>
}

fun voidedTweaksService(
    ignored: PackPersistence, categoryPersistence: CategoryPersistence
): VoidedTweaksService {
    return object : VoidedTweaksService {
        override fun getCategoriesByType(type: CategoryType): Either<DomainError, List<Category>> = either {
            categoryPersistence.getByType(type).bind()
        }

        override fun getAllCategories(): Either<DomainError, List<Category>> = either {
            categoryPersistence.getAll().bind()
        }

        override fun getPacksByCategory(categoryId: Short): Either<DomainError, List<Pack>> {
            TODO("Not yet implemented")
        }

        override fun getAllPacks(): Either<DomainError, List<Pack>> {
            TODO("Not yet implemented")
        }

        override fun getVersionsByPack(packId: Short): Either<DomainError, List<PackVersion>> {
            TODO("Not yet implemented")
        }

        override fun getAllVersions(): Either<DomainError, List<PackVersion>> {
            TODO("Not yet implemented")
        }

    }
}
