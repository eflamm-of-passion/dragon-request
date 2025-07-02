package io.eflamm.dragonrequest.domain.repository

import io.eflamm.dragonrequest.domain.model.ApiFile
import io.eflamm.dragonrequest.domain.model.Collection
import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.model.Workspace
import io.eflamm.dragonrequest.domain.model.common.Id

interface ApiFilesRepository {
    fun getAllApiFiles(): Result<List<ApiFile>>

    fun create(
        endpoint: Endpoint,
        parentId: Id,
    ): Result<Endpoint>

    fun create(
        collection: io.eflamm.dragonrequest.domain.model.Collection,
        parentId: Id,
    ): Result<Collection>

    fun create(workspace: Workspace): Result<Workspace>
}
