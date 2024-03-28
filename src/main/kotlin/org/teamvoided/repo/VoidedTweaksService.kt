package org.teamvoided.repo

interface VoidedTweaksService {
//    getAll
}

fun voidedTweaksService(packPersistence: PackPersistence): VoidedTweaksService {
    return object : VoidedTweaksService {}
}