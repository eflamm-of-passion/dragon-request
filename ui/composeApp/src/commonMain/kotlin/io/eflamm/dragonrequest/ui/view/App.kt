package io.eflamm.dragonrequest.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import io.eflamm.dragonrequest.ui.model.Endpoint
import io.eflamm.dragonrequest.ui.model.HttpMethod
import io.eflamm.dragonrequest.ui.viewmodel.EndpointViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun app(endpointViewModel: EndpointViewModel) {

    MaterialTheme {
        val selectedFile by endpointViewModel.selectedFile.collectAsState(initial = null)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Column(Modifier.fillMaxHeight().weight(1f).background(color = Color.Cyan)) {
                endpointList(endpointViewModel)
            }
            Column(Modifier.fillMaxHeight().weight(3f)) {
                endpointForm(endpointViewModel)
            }
        }
    }
}
