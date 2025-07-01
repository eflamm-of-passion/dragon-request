package io.eflamm.dragonrequest.application.usecase

import io.eflamm.dragonrequest.domain.model.Collection
import io.eflamm.dragonrequest.domain.model.common.Id
import io.eflamm.dragonrequest.domain.repository.CollectionRepository

class CollectionUseCases(
    private val repository: CollectionRepository,
) {
    fun create(collection: Collection, parentId: Id): Result<Collection> = repository.create(collection, parentId)
}
