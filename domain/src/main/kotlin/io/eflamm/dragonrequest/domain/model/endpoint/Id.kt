package io.eflamm.dragonrequest.domain.model.endpoint

import java.util.UUID

class Id private constructor(private val identifier: UUID) {

    companion object {
        fun create(): io.eflamm.dragonrequest.domain.model.endpoint.Id {
            return io.eflamm.dragonrequest.domain.model.endpoint.Id(UUID.randomUUID())
        }
        fun fromString(idFromString: String): io.eflamm.dragonrequest.domain.model.endpoint.Id {
            // TODO check it is an uuid
            return io.eflamm.dragonrequest.domain.model.endpoint.Id(UUID.fromString(idFromString))
        }
    }

    fun get(): String {
        return identifier.toString()
    }

}
