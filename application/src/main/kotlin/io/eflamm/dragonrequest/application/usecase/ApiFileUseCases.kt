package io.eflamm.dragonrequest.application.usecase

import io.eflamm.dragonrequest.domain.model.ApiFile
import io.eflamm.dragonrequest.domain.model.Collection
import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.model.Workspace
import io.eflamm.dragonrequest.domain.model.common.Id
import io.eflamm.dragonrequest.domain.repository.ApiFilesRepository

class ApiFileUseCases(
    private val repository: ApiFilesRepository,
) {
    fun getApiFiles(): Result<List<ApiFile>> = repository.getAllApiFiles()

    fun create(
        endpoint: Endpoint,
        parentId: Id,
    ): Result<Endpoint> = repository.create(endpoint, parentId)

    fun create(
        collection: Collection,
        parentId: Id,
    ): Result<Collection> = repository.create(collection, parentId)

    fun create(workspace: Workspace): Result<Workspace> = repository.create(workspace)
}
