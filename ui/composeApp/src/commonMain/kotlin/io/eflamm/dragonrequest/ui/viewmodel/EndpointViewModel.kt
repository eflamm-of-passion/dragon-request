package io.eflamm.dragonrequest.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
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

    private val _currentEndpoint = mutableStateOf<Endpoint?>(null)
    val currentEndpoint: Endpoint? get() = _currentEndpoint.value

    fun loadEndpoints() {
        viewModelScope.launch {
            val endpoints = async { endpointProvider.getAllEndpoints() }.await()
            _endpoints.emit(endpoints)
        }
    }

    fun selectEndpoint() {
        // TODO
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