package io.eflamm.dragonrequest.infrastructure.api.mapper

import io.eflamm.dragonrequest.domain.model.ApiFilename
import io.eflamm.dragonrequest.domain.model.Collection
import io.eflamm.dragonrequest.domain.model.common.Id
import io.eflamm.dragonrequest.infrastructure.api.CollectionCreateInput
import io.eflamm.dragonrequest.infrastructure.api.CollectionOutput

fun CollectionCreateInput.toCollection(): Collection = Collection(Id.create(), ApiFilename(name))

fun Collection.toCollectionOutput(): CollectionOutput =
    CollectionOutput(
        id.toString(),
        name.toString(),
        files = getFiles().map { it.toDto() },
    )
