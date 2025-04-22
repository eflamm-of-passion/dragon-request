package io.eflamm.dragonrequest.ui

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.lifecycle.viewmodel.compose.viewModel
import io.eflamm.dragonrequest.ui.view.app
import io.eflamm.dragonrequest.ui.viewmodel.EndpointViewModel
import io.eflamm.dragonrequest.ui.viewmodel.RestEndpointProvider
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
//    val endpointProvider = MockEndpointProvider()
    val endpointProvider = RestEndpointProvider("http://localhost:8080")
    ComposeViewport(document.body!!) {
        app(viewModel { EndpointViewModel(endpointProvider) })
    }
}