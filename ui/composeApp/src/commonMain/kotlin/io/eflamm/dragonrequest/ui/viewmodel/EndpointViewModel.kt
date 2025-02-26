package io.eflamm.dragonrequest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.eflamm.dragonrequest.ui.model.Endpoint
import kotlinx.coroutines.launch

class EndpointViewModel(private val endpointProvider: EndpointProvider): ViewModel() {
    private val _endpoints = mutableListOf<Endpoint>()
    val endpoints: List<Endpoint> get() = _endpoints

//    private val _currentEndpoint = mutableStateOf<Endpoint?>(null)
//    val currentEndpoint: Endpoint? get() = _currentEndpoint.value

    fun loadEndpoints() {
        viewModelScope.launch {
            _endpoints.addAll(endpointProvider.getAllEndpoints())
//            _currentEndpoint.value = _endpoints.first()
        }
    }

}