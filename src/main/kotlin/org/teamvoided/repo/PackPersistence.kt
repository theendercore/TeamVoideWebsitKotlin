package org.teamvoided.repo

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import org.teamvoided.DomainError
import org.teamvoided.GenericException
import org.teamvoided.PackNotFound
import org.teamvoided.database.Pack
import org.teamvoided.database.PackQueries
import java.net.URL

interface PackPersistence {
    fun create(category: Short, name: String, description: String, icon: URL): Either<DomainError, Short>
    fun getById(id: Short): Either<DomainError, Pack>
}


fun packPersistence(packQueries: PackQueries): PackPersistence {
    return object : PackPersistence {
        override fun create(category: Short, name: String, description: String, icon: URL): Either<DomainError, Short> =
            either {
                val x = packQueries.create(category, name, description, icon.toString()).executeAsOneOrNull()
                ensureNotNull(x) { GenericException(("Idk")) }
            }

        override fun getById(id: Short): Either<DomainError, Pack> = either {
            val x = packQueries.selectById(id, ::Pack).executeAsOneOrNull()
            ensureNotNull(x) { PackNotFound("Pack[$id] not found!") }
        }
    }
}
