package org.teamvoided.repo

import arrow.core.Either
import arrow.core.raise.either
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
                val packs = categories.map { packPersistence.getByCategory(it.id) }.bindAll().flatten()
                    .map { PackItem(it, PackVersion(1, 2, "1.20.0", "1.20.0", "https://google.com", "1.0.0")) }

                val cat = categories.map { CategoryItem(it, packs.filter { p -> p.categoryId == it.id }) }
                ensureNotNull(cat) { GenericException("Category failed!") }
            }

        override fun getAllCategories(): Either<DomainError, List<Category>> =
            either { categoryPersistence.getAll().bind() }

        override fun getPacksByCategory(categoryId: Short): Either<DomainError, List<Pack>> =
            either { packPersistence.getByCategory(categoryId).bind() }

        override fun getAllPacks(): Either<DomainError, List<Pack>> =
            either { packPersistence.getAll().bind() }

        override fun getPackById(id: Short): Either<DomainError, PackItem> = either {
            val pack = packPersistence.getById(id).bind()
            val packItem = PackItem(pack, PackVersion(1, pack.id, "1.20.0", "1.20.1", "https://google.com/dad", "1.0.0"))
            ensureNotNull(packItem) { GenericException("Pack failed!") }
        }

        override fun getVersionsByPack(packId: Short): Either<DomainError, List<PackVersion>> =
            either { versionPersistence.getByPack(packId).bind() }

        override fun getAllVersions(): Either<DomainError, List<PackVersion>> =
            either { versionPersistence.getAll().bind() }

    }
}
