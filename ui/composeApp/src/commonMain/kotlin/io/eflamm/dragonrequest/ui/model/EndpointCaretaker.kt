package io.eflamm.dragonrequest.ui.model

class EndpointCaretaker(
    initiatedEndpoint: Endpoint,
) {
    private val snapshots: MutableList<EndpointSnapshot> = mutableListOf()

    init {
        snapshots.add(initiatedEndpoint.saveSnapshot())
    }

    fun onEndpointEdited(endpoint: Endpoint) {
        snapshots.add(endpoint.saveSnapshot())
    }

    fun undo(): EndpointSnapshot {
        if (snapshots.size > 1) {
            snapshots.dropLast(1)
            return snapshots.last()
        } else {
            throw RuntimeException("Cannot undo the initial snapshot.")
        }
    }
}
