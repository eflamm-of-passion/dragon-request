package io.eflamm.dragonrequest.domain

import io.eflamm.dragonrequest.domain.model.RequestInput
import io.eflamm.dragonrequest.domain.model.RequestResult

interface RequestEngine {
    fun sendRequest(request: RequestInput): RequestResult
}
