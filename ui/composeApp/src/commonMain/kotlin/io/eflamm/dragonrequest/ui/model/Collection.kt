package io.eflamm.dragonrequest.ui.model

import io.eflamm.dragonrequest.ui.model.states.ApiFileState
import io.eflamm.dragonrequest.ui.model.states.Initiated

class Collection(
    id: String,
    state: ApiFileState,
    name: String,
) : InternalApiFile(id, name, state) {
    constructor(name: String) : this("", Initiated(), name)

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
