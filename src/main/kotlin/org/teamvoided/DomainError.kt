package org.teamvoided

sealed interface DomainError

data class GenericException(val exception: Exception) : DomainError

sealed interface PackError : DomainError
data class PackNotFound(val property: String) : PackError