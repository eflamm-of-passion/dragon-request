package io.eflamm.dragonrequest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.eflamm.dragonrequest.ui.model.ApiFile
import io.eflamm.dragonrequest.ui.model.Endpoint
import io.eflamm.dragonrequest.ui.model.EndpointState
import io.eflamm.dragonrequest.ui.model.HttpMethod
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EndpointViewModel(
    private val endpointProvider: EndpointProvider,
) : ViewModel() {
    private val _endpoints = MutableStateFlow<List<Endpoint>>(emptyList())
    val endpoints: StateFlow<List<Endpoint>> = _endpoints.asStateFlow()

    private val _currentEndpoint = MutableStateFlow<Endpoint?>(null)
    val currentEndpoint: StateFlow<Endpoint?> = _currentEndpoint.asStateFlow()

    fun getEndpoints() {
        viewModelScope.launch {
            // TODO get all the endpoints from local
            // TODO get all the endpoints from API
            // TODO synchronize all the endpoints from API with locals

            val endpointsFromApi = async { endpointProvider.getAllEndpoints() }.await()
            if (endpointsFromApi.isSuccess) {
                _endpoints.emit(endpointsFromApi.getOrNull() ?: emptyList())
            } else {
                TODO("display an error message")
            }
        }
    }

    fun selectEndpoint(selectedEndpoint: Endpoint) {
        _currentEndpoint.value = selectedEndpoint
    }

    fun initEndpoint(parentFile: ApiFile) {
        val initiatedEndpoint =
            Endpoint(
                id = "",
                name = "New endpoint",
                httpMethod = HttpMethod.GET,
                url = "https://example.org",
                state = EndpointState.Initiated,
            )
        parentFile.addFile(initiatedEndpoint)
        this.selectEndpoint(initiatedEndpoint)
    }

    fun createNewEndpoint(endpointToCreate: Endpoint) {
        viewModelScope.launch {
            // TODO create the endpoint using the API
            // TODO transform the initiated endpoint in a saved endpoint

            val newEndpoint = endpointProvider.createEndpoint(endpointToCreate)
//            _endpoints.emit(_endpoints.value + newEndpointState)
        }
    }

    fun updateSavedEndpoint(endpointToUpdate: Endpoint) {
        viewModelScope.launch {
            // TODO send the endpoint to the API
            // TODO update the endpoint with the response
        }
    }

    fun saveEndpointOnRemote(endpointToSave: Endpoint) {
        viewModelScope.launch {
            // TODO when initiated endpoint then create
            // TODO when saved endpoint then update
        }
    }

    fun deleteEndpoint(endpointToDelete: Endpoint) {
        viewModelScope.launch {
            // TODO when saved endpoint then delete using the API
            // TODO remove from the list
            endpointProvider.deleteEndpoint(endpointToDelete)
            getEndpoints()
        }
    }
}
