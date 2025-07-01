package io.eflamm.dragonrequest.infrastructure.api.mapper

import io.eflamm.dragonrequest.domain.model.ApiFile
import io.eflamm.dragonrequest.domain.model.Collection
import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.model.Workspace
import io.eflamm.dragonrequest.infrastructure.api.ApiFileOutput

fun ApiFile.toDto(): ApiFileOutput =
    when (this) {
        is Workspace -> toWorkspaceOutput()
        is Collection -> toCollectionOutput()
        is Endpoint -> toEndpointOutput()
        else -> throw IllegalStateException("Unknown ApiFile type")
    } as ApiFileOutput

fun List<ApiFile>.toDto(): List<ApiFileOutput> = map { it.toDto() }
