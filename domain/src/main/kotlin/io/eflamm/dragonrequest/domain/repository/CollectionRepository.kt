package io.eflamm.dragonrequest.domain.repository

import io.eflamm.dragonrequest.domain.model.Collection
import io.eflamm.dragonrequest.domain.model.common.Id

interface CollectionRepository {
    fun create(
        collection: Collection,
        parentId: Id,
    ): Result<Collection>
}
