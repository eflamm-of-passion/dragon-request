package io.eflamm.dragonrequest.application.usecase

import io.eflamm.dragonrequest.domain.model.Workspace
import io.eflamm.dragonrequest.domain.repository.WorkspaceRepository

class WorkspaceUseCases(
    private val repository: WorkspaceRepository,
) {
    fun create(workspace: Workspace): Result<Workspace> = repository.create(workspace)
}
