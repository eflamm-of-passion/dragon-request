package io.eflamm.dragonrequest.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.eflamm.dragonrequest.ui.model.Endpoint
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EndpointViewModel(private val endpointProvider: EndpointProvider): ViewModel() {
    private val _endpoints = MutableStateFlow<List<Endpoint>>(emptyList())
    val endpoints: StateFlow<List<Endpoint>> = _endpoints.asStateFlow()

    private val _currentEndpoint = MutableStateFlow<Endpoint?>(null)
    val currentEndpoint: StateFlow<Endpoint?> = _currentEndpoint.asStateFlow()

    fun loadEndpoints() {
        viewModelScope.launch {
            val endpoints = async { endpointProvider.getAllEndpoints() }.await()
            _endpoints.emit(endpoints)
        }
    }

    fun selectEndpoint(selectedEndpointId: String) {
        val selectedEndpoint = _endpoints.value.find { it.id == selectedEndpointId }
        println(selectedEndpoint?.url)
        _currentEndpoint.value = selectedEndpoint
    }

    fun createEndpoint() {
        // TODO
    }

    fun updateEndpoint() {
        // TODO
    }

    fun deleteEndpoint() {
        // TODO
    }

}