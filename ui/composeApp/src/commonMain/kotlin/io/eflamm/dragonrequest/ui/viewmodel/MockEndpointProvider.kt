package io.eflamm.dragonrequest.ui.viewmodel

import io.eflamm.dragonrequest.ui.model.Endpoint

class MockEndpointProvider: EndpointProvider {
    override fun getAllEndpoints(): List<Endpoint> = listOf(
            Endpoint("1", "GET", "https://www.google.com", false),
            Endpoint("2", "POST", "https://www.youtube.com", false)
    )
}