package io.eflamm.dragonrequest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.eflamm.dragonrequest.ui.model.EndpointState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EndpointViewModel(private val endpointProvider: EndpointProvider): ViewModel() {
    private val _endpoints = MutableStateFlow<List<EndpointState>>(emptyList())
    val endpoints: StateFlow<List<EndpointState>> = _endpoints.asStateFlow()

    private val _currentEndpoint = MutableStateFlow<EndpointState?>(null)
    val currentEndpoint: StateFlow<EndpointState?> = _currentEndpoint.asStateFlow()

    fun loadEndpoints() {
        viewModelScope.launch {
            val endpointsFromApi = async { endpointProvider.getAllEndpoints() }.await()
            val endpointStates = endpointsFromApi.map { EndpointState(it) }
            _endpoints.emit(endpointStates)
        }
    }

    fun selectEndpoint(selectedEndpointId: String) {
        val selectedEndpointState = _endpoints.value.find { endpoint -> endpoint.initial.id == selectedEndpointId }
        _currentEndpoint.value = selectedEndpointState
    }

    fun createEndpoint(endpointState: EndpointState) {
        viewModelScope.launch {
            val newEndpoint = endpointProvider.createEndpoint(endpointState.modified)
            val newEndpointState = EndpointState(newEndpoint)
            _endpoints.emit(_endpoints.value + newEndpointState)
        }
    }

    fun saveEndpoint(endpointToSave: EndpointState) {
        viewModelScope.launch {
            if (!endpointToSave.isSavedOnRemote) {
                EndpointState(endpointProvider.createEndpoint(endpointToSave.modified))
            } else {
                EndpointState(endpointProvider.updateEndpoint(endpointToSave.modified))
            }
        }
    }

    fun deleteEndpoint(endpointToDelete: EndpointState) {
        viewModelScope.launch {
            endpointProvider.deleteEndpoint(endpointToDelete.initial)
        }
    }

}