package io.eflamm.dragonrequest.ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.lifecycle.viewmodel.compose.viewModel
import io.eflamm.dragonrequest.ui.view.App
import io.eflamm.dragonrequest.ui.viewmodel.EndpointViewModel
import io.eflamm.dragonrequest.ui.viewmodel.RestEndpointProvider

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Dragon reQuest UI",
    ) {
        val endpointProvider = RestEndpointProvider()
        App(viewModel { EndpointViewModel(endpointProvider) })
    }
}