package io.eflamm.dragonrequest.domain

interface RequestEngine {
    fun sendRequest(request: Any)
}