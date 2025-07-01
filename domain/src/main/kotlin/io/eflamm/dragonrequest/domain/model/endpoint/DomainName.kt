package io.eflamm.dragonrequest.domain.model.endpoint

@JvmInline
value class DomainName(
    val value: String,
) {
    fun get(): String = value

    override fun toString(): String = value
}
