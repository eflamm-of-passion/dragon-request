package io.eflamm.dragonrequest.kotlinscriptengine.loader

import java.io.FileNotFoundException

class ResourceScriptLoader : ScriptLoader {
    override fun load(path: String): String {
        return object {}.javaClass.classLoader.getResource(path)?.readText()
            ?: throw FileNotFoundException(path)
    }
}