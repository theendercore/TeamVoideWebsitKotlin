package org.teamvoided.repo

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import org.teamvoided.CategoryNotFound
import org.teamvoided.DomainError
import org.teamvoided.GenericException
import org.teamvoided.data.CategoryType
import org.teamvoided.database.Category
import org.teamvoided.database.CategoryQueries

interface CategoryPersistence {
    fun create(name: String, type: CategoryType): Either<DomainError, Short>
    fun getById(id: Short): Either<DomainError, Category>
    fun getByType(type: CategoryType): Either<DomainError, List<Category>>

    fun getAll(): Either<DomainError, List<Category>>
}

fun categoryPersistence(categoryQueries: CategoryQueries): CategoryPersistence {
    return object : CategoryPersistence {
        override fun create(name: String, type: CategoryType): Either<DomainError, Short> = either {
            val id = categoryQueries.create(name, type.toString()).executeAsOneOrNull()
            ensureNotNull(id) { GenericException("Error creating category $name") }
        }

        override fun getById(id: Short): Either<DomainError, Category> = either {
            val category = categoryQueries.selectById(id, ::Category).executeAsOneOrNull()
            ensureNotNull(category) { CategoryNotFound("Category[$id] not found!") }
        }

        override fun getByType(type: CategoryType): Either<DomainError, List<Category>> = either {
            val categoryList = categoryQueries.selectByType(type.toString()).executeAsList()
            ensure(categoryList.isNotEmpty()) { CategoryNotFound("No Categories with Type[$type]!") }
            ensureNotNull(categoryList) { CategoryNotFound("No Categories with Type[$type]!") }
        }

        override fun getAll(): Either<DomainError, List<Category>> = either {
            val categoryList = categoryQueries.selecAll().executeAsList()
            ensure(categoryList.isNotEmpty()) { CategoryNotFound("No Categories where found!") }
            ensureNotNull(categoryList) { CategoryNotFound("No Categories where found!!") }
        }
    }
}
