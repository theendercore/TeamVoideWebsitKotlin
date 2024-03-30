package org.teamvoided

sealed interface DomainError

data class GenericException(val message: String) : DomainError

sealed interface PackError : DomainError
data class PackNotFound(val message: String) : PackError


sealed interface CategoryError : DomainError

data class CategoryNotFound(val message: String) : CategoryError
