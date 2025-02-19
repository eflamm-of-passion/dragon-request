package io.eflamm.dragonrequest.ui

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform