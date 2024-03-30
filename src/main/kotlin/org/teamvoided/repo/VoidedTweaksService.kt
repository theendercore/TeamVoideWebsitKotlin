package org.teamvoided.repo

import arrow.core.Either
import arrow.core.raise.either
import org.teamvoided.DomainError
import org.teamvoided.data.CategoryType
import org.teamvoided.database.Category

interface VoidedTweaksService {
    fun getCategories(type: CategoryType): Either<DomainError, List<Category>>
    fun getAllCategories(): Either<DomainError, List<Category>>
}

fun voidedTweaksService(
    ignored: PackPersistence, categoryPersistence: CategoryPersistence
): VoidedTweaksService {
    return object : VoidedTweaksService {
        override fun getCategories(type: CategoryType): Either<DomainError, List<Category>> = either {
            categoryPersistence.getByType(type).bind()
        }

        override fun getAllCategories(): Either<DomainError, List<Category>> = either {
            categoryPersistence.getAll().bind()
        }

    }
}
