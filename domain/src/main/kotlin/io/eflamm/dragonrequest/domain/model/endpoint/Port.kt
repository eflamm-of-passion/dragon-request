package io.eflamm.dragonrequest.domain.model.endpoint

@JvmInline
value class Port(
    val port: Int = 80,
) {
    fun get(): Int = port

    fun isDefaultPort(): Boolean = port == 80 || port == 443

    override fun toString(): String = port.toString()
}
