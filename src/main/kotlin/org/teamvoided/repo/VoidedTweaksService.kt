package org.teamvoided.repo

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import org.teamvoided.DomainError
import org.teamvoided.GenericException
import org.teamvoided.data.CategoryItem
import org.teamvoided.data.CategoryType
import org.teamvoided.data.PackItem
import org.teamvoided.database.Category
import org.teamvoided.database.Pack
import org.teamvoided.database.PackVersion

interface VoidedTweaksService {
    fun getCategoriesByTypeOld(type: CategoryType): Either<DomainError, List<Category>>
    fun getCategoriesByType(type: CategoryType): Either<DomainError, List<CategoryItem>>
    fun getAllCategories(): Either<DomainError, List<Category>>

    fun getPacksByCategory(categoryId: Short): Either<DomainError, List<Pack>>
    fun getAllPacks(): Either<DomainError, List<Pack>>
    fun getPackById(id: Short): Either<DomainError, PackItem>

    fun getVersionsByPack(packId: Short): Either<DomainError, List<PackVersion>>
    fun getAllVersions(): Either<DomainError, List<PackVersion>>
}

fun voidedTweaksService(
    categoryPersistence: CategoryPersistence,
    packPersistence: PackPersistence,
    versionPersistence: VersionPersistence
): VoidedTweaksService {
    return object : VoidedTweaksService {
        override fun getCategoriesByTypeOld(type: CategoryType): Either<DomainError, List<Category>> =
            either { categoryPersistence.getByType(type).bind() }

        override fun getCategoriesByType(type: CategoryType): Either<DomainError, List<CategoryItem>> =
            either {
                val categories = categoryPersistence.getByType(type).bind()
                    .mapNotNull {
                        packPersistence.getByCategory(it.id)
                            .getOrElse { e -> println(e); null }
                            ?.map { pack -> PackItem(pack, pv(pack.id)) }
                            ?.let { packs -> CategoryItem(it, packs) }
                    }
                ensure(categories.isNotEmpty()) { GenericException("No Categories Processed!") }
                ensureNotNull(categories) { GenericException("Category failed!") }
            }

        override fun getAllCategories(): Either<DomainError, List<Category>> =
            either { categoryPersistence.getAll().bind() }

        override fun getPacksByCategory(categoryId: Short): Either<DomainError, List<Pack>> =
            either { packPersistence.getByCategory(categoryId).bind() }

        override fun getAllPacks(): Either<DomainError, List<Pack>> =
            either { packPersistence.getAll().bind() }

        override fun getPackById(id: Short): Either<DomainError, PackItem> = either {
            val pack = packPersistence.getById(id)
                .map { PackItem(it, pv(it.id)) }
                .bind()
            ensureNotNull(pack) { GenericException("Pack failed!") }
        }

        override fun getVersionsByPack(packId: Short): Either<DomainError, List<PackVersion>> =
            either { versionPersistence.getByPack(packId).bind() }

        override fun getAllVersions(): Either<DomainError, List<PackVersion>> =
            either { versionPersistence.getAll().bind() }

    }
}


private fun pv(id: Short) = PackVersion(1, id, "1.20.0", "1.20.0", "https://google.com", "1.0.0")
