package io.eflamm.dragonrequest.domain.repository

import io.eflamm.dragonrequest.domain.model.ApiFile

interface ApiFilesRepository {
    fun getAllApiFiles(): Result<List<ApiFile>>
}
