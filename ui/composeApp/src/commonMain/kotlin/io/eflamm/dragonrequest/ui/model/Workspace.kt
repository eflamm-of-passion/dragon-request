package io.eflamm.dragonrequest.ui.model

import io.eflamm.dragonrequest.ui.model.states.ApiFileState
import io.eflamm.dragonrequest.ui.model.states.Initiated

open class Workspace(
    id: String,
    state: ApiFileState,
    name: String,
) : InternalApiFile(id, name, state) {
    constructor() : this("", Initiated(), "New workspace")
    constructor(name: String) : this("", Initiated(), name)

    // TODO implement memento and states, because it has to be saved in the repo as well

    override fun addFile(fileToAdd: InternalApiFile): Result<Int> =
        when (fileToAdd) {
            is Endpoint -> super.addFile(fileToAdd)
            is Collection -> super.addFile(fileToAdd)
            else -> Result.failure(IllegalArgumentException("${fileToAdd::class} cannot be added to ${this::class}"))
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
