package io.eflamm.dragonrequest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.eflamm.dragonrequest.ui.model.Endpoint
import kotlinx.coroutines.launch

class EndpointViewModel: ViewModel() {
    private val _endpoints = mutableListOf<Endpoint>()
    val endpoints: List<Endpoint> get() = _endpoints

//    private val _currentEndpoint = mutableStateOf<Endpoint?>(null)
//    val currentEndpoint: Endpoint? get() = _currentEndpoint.value

    fun loadEndpoints() {
        viewModelScope.launch {
            _endpoints.addAll(
                listOf(
                    Endpoint("1", "GET", "https://www.google.com", false),
                    Endpoint("2", "POST", "https://www.youtube.com", false)
                )
            )
//            _currentEndpoint.value = _endpoints.first()
        }
    }

}