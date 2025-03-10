package io.eflamm.dragonrequest.ui.model

fun EndpointState(initialEndpoint: Endpoint) = EndpointState(initialEndpoint, initialEndpoint, false)

data class EndpointState(
    val initial: Endpoint,
    val modified: Endpoint,
    val hasChanges: Boolean = false,
    val isSavedOnRemote: Boolean = true
) {
    fun update(endpoint: Endpoint): EndpointState {
        return copy(modified = endpoint, hasChanges = initial != endpoint)
    }
}