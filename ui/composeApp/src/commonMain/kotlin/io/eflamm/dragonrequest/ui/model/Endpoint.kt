package io.eflamm.dragonrequest.ui.model

import io.eflamm.dragonrequest.ui.model.states.ApiFileState
import io.eflamm.dragonrequest.ui.model.states.Initiated

class Endpoint(
    id: String,
    state: ApiFileState,
    name: String,
    var httpMethod: HttpMethod,
    var url: String,
) : InternalApiFile(id, name, state) {
    // TODO edit endpoint, save endpoint, save endpoint and synchronize it to repo

    constructor() : this(id = "", state = Initiated(), name = "New endpoint", httpMethod = HttpMethod.GET, url = "https://example.org")

    fun saveSnapshot(): EndpointSnapshot =
        EndpointSnapshot(
            state = state,
            name = name,
            httpMethod = httpMethod,
            url = url,
        )


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
