package io.eflamm.dragonrequest.domain.repository

import io.eflamm.dragonrequest.domain.model.Workspace

interface WorkspaceRepository {
    fun create(workspace: Workspace): Result<Workspace>
}
