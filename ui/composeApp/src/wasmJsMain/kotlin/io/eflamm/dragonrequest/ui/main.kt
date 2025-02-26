package io.eflamm.dragonrequest.ui

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.lifecycle.viewmodel.compose.viewModel
import io.eflamm.dragonrequest.ui.view.App
import io.eflamm.dragonrequest.ui.viewmodel.EndpointViewModel
import io.eflamm.dragonrequest.ui.viewmodel.MockEndpointProvider
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // TODO give an implementation of the API client
    val endpointProvider = MockEndpointProvider()
    ComposeViewport(document.body!!) {
        App(viewModel { EndpointViewModel(endpointProvider) })
    }
}