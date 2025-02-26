package io.eflamm.dragonrequest.ui.viewmodel

import io.eflamm.dragonrequest.ui.model.Endpoint

interface EndpointProvider {
    fun getAllEndpoints(): List<Endpoint>
}