package io.eflamm.dragonrequest.domain.model.common

import java.util.UUID

@JvmInline
value class Id private constructor(
    private val identifier: UUID,
) {
    companion object {
        fun create(): Id = Id(UUID.randomUUID())

        fun fromString(idFromString: String): Id {
            // TODO check it is an uuid
            return Id(UUID.fromString(idFromString))
        }
    }

    override fun toString(): String = identifier.toString()
}
