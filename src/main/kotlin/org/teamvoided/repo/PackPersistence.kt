package org.teamvoided.repo

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import org.teamvoided.DomainError
import org.teamvoided.GenericException
import org.teamvoided.PackNotFound
import org.teamvoided.database.Pack
import org.teamvoided.database.PackQueries
import java.net.URL

interface PackPersistence {
    fun getById(id: Short): Either<DomainError, Pack>
    fun getByCategory(categoryId: Short): Either<DomainError, List<Pack>>
    fun getAll(): Either<DomainError, List<Pack>>
    fun create(category: Short, name: String, description: String, icon: URL): Either<DomainError, Short>
    fun deleteById(id: Short): Either<DomainError, Short>
}


fun packPersistence(packQueries: PackQueries): PackPersistence {
    return object : PackPersistence {

        override fun getById(id: Short): Either<DomainError, Pack> = either {
            val pack = packQueries.selectById(id, ::Pack).executeAsOneOrNull()
            ensureNotNull(pack) { PackNotFound("Pack[$id] not found!") }
        }

        override fun getByCategory(categoryId: Short): Either<DomainError, List<Pack>> = either {
            val packs = packQueries.selectByCategoryId(categoryId, ::Pack).executeAsList()
            ensure(packs.isNotEmpty()) { PackNotFound("Pack for Category[$categoryId] found!") }
            ensureNotNull(packs) { PackNotFound("Pack for Category[$categoryId] found!") }
        }

        override fun getAll(): Either<DomainError, List<Pack>> = either {
            val packs = packQueries.selectAll().executeAsList()
            ensure(packs.isNotEmpty()) { PackNotFound("No Packs where found!") }
            ensureNotNull(packs) { PackNotFound("No Packs where found!") }
        }

        override fun create(category: Short, name: String, description: String, icon: URL): Either<DomainError, Short> =
            either {
                val packId = packQueries.create(category, name, description, icon.toString()).executeAsOneOrNull()
                ensureNotNull(packId) { GenericException("Could not make Pack[$name]") }
            }

        override fun deleteById(id: Short): Either<DomainError, Short> = either {
            val deletedId = packQueries.delete(id).executeAsOneOrNull()
            ensureNotNull(deletedId) { GenericException("Could not delete $deletedId!") }
        }
    }
}
