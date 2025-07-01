package io.eflamm.dragonrequest.application.usecase

import io.eflamm.dragonrequest.domain.model.ApiFile
import io.eflamm.dragonrequest.domain.repository.ApiFilesRepository

class ApiFileUseCases(
    private val apiFilesRepository: ApiFilesRepository,
) {
    fun getApiFiles(): Result<List<ApiFile>> = apiFilesRepository.getAllApiFiles()
}
