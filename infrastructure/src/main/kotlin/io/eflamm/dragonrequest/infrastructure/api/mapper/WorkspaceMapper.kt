package io.eflamm.dragonrequest.infrastructure.api.mapper

import io.eflamm.dragonrequest.domain.model.ApiFilename
import io.eflamm.dragonrequest.domain.model.Workspace
import io.eflamm.dragonrequest.domain.model.common.Id
import io.eflamm.dragonrequest.infrastructure.api.WorkspaceCreateInput
import io.eflamm.dragonrequest.infrastructure.api.WorkspaceOutput

fun WorkspaceCreateInput.toWorkspace(): Workspace = Workspace(Id.create(), ApiFilename(name))

fun Workspace.toWorkspaceOutput(): WorkspaceOutput =
    WorkspaceOutput(
        id.toString(),
        name.toString(),
        files = getFiles().map { it.toDto() },
    )
