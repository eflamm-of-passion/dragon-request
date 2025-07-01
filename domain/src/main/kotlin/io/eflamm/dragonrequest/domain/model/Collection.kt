package io.eflamm.dragonrequest.domain.model

import io.eflamm.dragonrequest.domain.model.common.Id

class Collection(
    id: Id,
    name: ApiFilename,
) : InternalApiFile(id, name) {
    override fun addFile(fileToAdd: InternalApiFile): Result<Int> =
        when (fileToAdd) {
            is Endpoint -> super.addFile(fileToAdd)
            is Collection -> super.addFile(fileToAdd)
            else -> Result.failure(IllegalArgumentException("${fileToAdd::class} cannot be added to ${this::class}"))
        }
}
