package io.eflamm.domain.model.endpoint

import java.util.UUID

class Id private constructor(private val identifier: UUID) {

    companion object {
        fun create(): Id {
            return Id(UUID.randomUUID())
        }
        fun fromString(idFromString: String): Id {
            // TODO check it is an uuid
            return Id(UUID.fromString(idFromString))
        }
    }

    fun get(): String {
        return identifier.toString()
    }

}
