package io.eflamm.dragonrequest.ui.viewmodel

import io.eflamm.dragonrequest.ui.model.Endpoint
import io.eflamm.dragonrequest.ui.model.EndpointState
import io.eflamm.dragonrequest.ui.model.HttpMethod

class MockEndpointProvider : EndpointProvider {
    private var endpoints =
        mutableListOf(
            Endpoint("1", EndpointState.SavedRemoteUnedited, "Google", HttpMethod.GET, "https://www.google.com"),
            Endpoint("2", EndpointState.SavedRemoteUnedited, "Youtube", HttpMethod.POST, "https://www.youtube.com"),
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
