package io.eflamm.dragonrequest.ui.viewmodel

import io.eflamm.dragonrequest.ui.model.Endpoint
import io.eflamm.dragonrequest.ui.model.HttpMethod
import io.eflamm.dragonrequest.ui.model.states.SavedRemoteUnedited

class MockEndpointProvider : EndpointProvider {
    private var endpoints =
        mutableListOf(
            Endpoint(
                id = "1",
                state = SavedRemoteUnedited(),
                name = "Google",
                httpMethod = HttpMethod.GET,
                url = "https://www.google.com",
            ),
            Endpoint(
                id = "2",
                state = SavedRemoteUnedited(),
                name = "Youtube",
                httpMethod = HttpMethod.POST,
                url = "https://www.youtube.com",
            ),
        )

    override suspend fun getAllEndpoints(): Result<List<Endpoint>> = Result.success(endpoints)

    override suspend fun createEndpoint(endpointToCreate: Endpoint): Result<Endpoint> {
        endpoints.add(endpointToCreate)
        return Result.success(endpointToCreate)
    }

    override suspend fun updateEndpoint(endpointToUpdate: Endpoint): Result<Endpoint> {
        endpoints =
            endpoints
                .map {
                    if (endpointToUpdate.id == it.id) {
                        endpointToUpdate
                    } else {
                        it
                    }
                }.toMutableList()
        return Result.success(endpointToUpdate)
    }

    override suspend fun deleteEndpoint(endpointToDelete: Endpoint): Result<Unit> {
        endpoints = endpoints.filter { endpointToDelete.id != it.id }.toMutableList()
        return Result.success(Unit)
    }
}
