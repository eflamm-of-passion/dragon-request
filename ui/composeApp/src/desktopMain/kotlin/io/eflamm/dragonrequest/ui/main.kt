package io.eflamm.dragonrequest.ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.eflamm.dragonrequest.ui.view.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Dragon reQuest UI",
    ) {
        App()
    }
}