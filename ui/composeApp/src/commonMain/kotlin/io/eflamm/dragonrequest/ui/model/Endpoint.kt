package io.eflamm.dragonrequest.ui.model

import io.eflamm.dragonrequest.ui.model.EndpointState.Initiated
import io.eflamm.dragonrequest.ui.model.EndpointState.SavedLocalEdited
import io.eflamm.dragonrequest.ui.model.EndpointState.SavedLocalUnedited
import io.eflamm.dragonrequest.ui.model.EndpointState.SavedRemoteEdited
import io.eflamm.dragonrequest.ui.model.EndpointState.SavedRemoteUnedited

class Endpoint(
    var id: String,
    private var state: EndpointState = Initiated,
    var name: String,
    var httpMethod: HttpMethod,
    var url: String,
) : InternalApiFile() {
    // TODO edit endpoint, save endpoint, save endpoint and synchronize it to repo

    fun changeState(nextState: EndpointState) {
        if (canChangeToState(nextState).isSuccess) {
            this.state = nextState
        }
    }

    private fun canChangeToState(nextState: EndpointState): Result<Unit> {
        val canChange =
            when (this.state) {
                is Initiated -> nextState == SavedLocalEdited
                SavedLocalUnedited -> setOf(SavedLocalEdited, SavedRemoteUnedited).contains(nextState)
                SavedLocalEdited -> setOf(SavedLocalUnedited, SavedRemoteUnedited).contains(nextState)
                SavedRemoteUnedited -> nextState == SavedRemoteEdited
                SavedRemoteEdited -> nextState == SavedRemoteUnedited
            }
        return if (canChange) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Cannot go from ${this.state} to $nextState"))
        }
    }

    fun saveSnapshot(): EndpointSnapshot =
        EndpointSnapshot(
            state = state,
            name = name,
            httpMethod = httpMethod,
            url = url,
        )

    fun restoreSnapshot(snapshotToRestore: EndpointSnapshot) {
        state = snapshotToRestore.state
        name = snapshotToRestore.name
        httpMethod = snapshotToRestore.httpMethod
        url = snapshotToRestore.url
    }

    override fun addFile(fileToAdd: InternalApiFile): Result<Int> {
        TODO("Not yet implemented")
    }

    override fun moveFile() {
        TODO("Not yet implemented")
    }

    override fun removeFile() {
        TODO("Not yet implemented")
    }

    override fun nextFileIndex(): Int {
        TODO("Not yet implemented")
    }
}

enum class HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    OPTIONS,
}
