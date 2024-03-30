package org.teamvoided.repo

import arrow.core.Either
import arrow.core.raise.either
import org.teamvoided.DomainError
import org.teamvoided.data.CategoryType

interface CreatorService {
    fun addCategory(name: String, type: CategoryType): Either<DomainError, Short>

    fun deleteCategory(id: Short): DomainError?
}

fun creatorService(
    ignored: PackPersistence, categoryPersistence: CategoryPersistence
): CreatorService {
    return object : CreatorService {
        override fun addCategory(name: String, type: CategoryType): Either<DomainError, Short> = either {
            categoryPersistence.create(name, type).bind()
        }

        override fun deleteCategory(id: Short): DomainError? {
            return categoryPersistence.deleteById(id).leftOrNull()
        }
    }
}
