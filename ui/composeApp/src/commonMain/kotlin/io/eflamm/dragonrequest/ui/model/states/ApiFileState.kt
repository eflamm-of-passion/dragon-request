package io.eflamm.dragonrequest.ui.model.states

interface ApiFileState {
//    fun init(): ApiFileState
    fun edit(): ApiFileState

    fun saveOnLocal(): ApiFileState

    fun saveOnRemote(): ApiFileState
}
