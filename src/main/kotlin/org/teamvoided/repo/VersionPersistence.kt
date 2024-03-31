package org.teamvoided.repo

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import org.teamvoided.DomainError
import org.teamvoided.GenericException
import org.teamvoided.VersionNotFound
import org.teamvoided.database.PackVersion
import org.teamvoided.database.PackVersionQueries
import java.net.URL

interface VersionPersistence {
    fun getById(id: Short): Either<DomainError, PackVersion>
    fun getByPack(packId: Short): Either<DomainError, List<PackVersion>>

    fun getAll(): Either<DomainError, List<PackVersion>>
    fun create(
        packId: Short, minVersion: String, maxVersion: String, url: URL, version: String
    ): Either<DomainError, Short>

    fun deleteById(id: Short): Either<DomainError, Short>
}


fun versionPersistence(versionQueries: PackVersionQueries): VersionPersistence {
    return object : VersionPersistence {

        override fun getById(id: Short): Either<DomainError, PackVersion> = either {
            val pack = versionQueries.selectById(id, ::PackVersion).executeAsOneOrNull()
            ensureNotNull(pack) { VersionNotFound("Pack[$id] not found!") }
        }

        override fun getByPack(packId: Short): Either<DomainError, List<PackVersion>> = either {
            val packs = versionQueries.selectByPackId(packId, ::PackVersion).executeAsList()
            ensure(packs.isNotEmpty()) { VersionNotFound("Pack for Category[$packId] found!") }
            ensureNotNull(packs) { VersionNotFound("Pack for Category[$packId] found!") }
        }

        override fun getAll(): Either<DomainError, List<PackVersion>> = either {
            val packs = versionQueries.selectAll().executeAsList()
            ensure(packs.isNotEmpty()) { VersionNotFound("No Packs where found!") }
            ensureNotNull(packs) { VersionNotFound("No Packs where found!") }
        }

        override fun create(
            packId: Short, minVersion: String, maxVersion: String, url: URL, version: String
        ): Either<DomainError, Short> = either {
            val versionId =
                versionQueries.create(packId, minVersion, maxVersion, url.toString(), version).executeAsOneOrNull()
            ensureNotNull(versionId) { GenericException("Could not create Version[$version] for Pack[$packId]") }
        }

        override fun deleteById(id: Short): Either<DomainError, Short> = either {
            val deletedId = versionQueries.delete(id).executeAsOneOrNull()
            ensureNotNull(deletedId) { GenericException("Could not delete $deletedId!") }
        }
    }
}
