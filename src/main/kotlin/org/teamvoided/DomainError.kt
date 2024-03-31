package org.teamvoided

import kotlinx.serialization.Serializable

@Serializable
sealed interface DomainError

@Serializable
data class GenericException(val message: String) : DomainError

@Serializable
sealed interface PackError : DomainError

@Serializable
data class PackNotFound(val message: String) : PackError

@Serializable
sealed interface VersionError : DomainError

@Serializable
data class VersionNotFound(val message: String) : VersionError

@Serializable
sealed interface CategoryError : DomainError


@Serializable
data class CategoryNotFound(val message: String) : CategoryError
