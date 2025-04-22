package io.eflamm.dragonrequest.ui.model

sealed interface EndpointState {
    /**
     * Endpoint is just created
     */
    data object Initiated : EndpointState

    /**
     * Endpoint has been saved, on local device only
     */
    data object SavedLocalUnedited : EndpointState

    /**
     * Endpoint is saved, on local device only, and has modifications
     */
    data object SavedLocalEdited : EndpointState

    /**
     * Endpoint has been saved, on the repository
     */
    data object SavedRemoteUnedited : EndpointState

    /**
     * Endpoint has been saved, on the repository, and has modifications
     */
    data object SavedRemoteEdited : EndpointState
}
