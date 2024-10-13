package io.eflamm.io.eflamm.domain.model.endpoint

import java.util.UUID

class Id private constructor(private val identifier: UUID) {

    companion object {
        fun create(): Id {
            return Id(UUID.randomUUID())
        }
    }

    fun get(): String {
        return identifier.toString()
    }

}
