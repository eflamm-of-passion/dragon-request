package io.eflamm.dragonrequest.kotlinscriptengine.loader

interface ScriptLoader {
    fun load(path: String): String
}