package io.eflamm.dragonrequest.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.eflamm.dragonrequest.ui.model.Endpoint
import io.eflamm.dragonrequest.ui.model.HttpMethod
import io.eflamm.dragonrequest.ui.viewmodel.EndpointViewModel

@Composable
fun endpointForm(
    endpointViewModel: EndpointViewModel,
) {
    Column(
        Modifier.fillMaxSize().background(color = Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        formBar(endpointViewModel)
        formButtons(endpointViewModel)
    }
}

@Composable
private fun formBar(
    endpointViewModel: EndpointViewModel,
) {
    Row(Modifier.fillMaxWidth(0.8f).height(50.dp)) {
        httpMethodField(endpointViewModel)
        Column(Modifier.fillMaxWidth(0.9f).fillMaxHeight()) {
            TextField(
                // FIXME
//                    value = currentEndpoint?.modified?.url ?: "",
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
            )
        }
    }
}

@Composable
private fun formButtons(
    endpointViewModel: EndpointViewModel
) {
    Row(Modifier.fillMaxWidth(0.5f), horizontalArrangement = Arrangement.SpaceAround) {
        Button(onClick = {}, colors = sendButtonColors()) {
            Text("Send")
        }
        Button(
            onClick = { },
            colors = saveButtonColors(),
        ) {
            Text("Save")
        }
        Button(
            onClick = { },
            colors = deleteButtonColors(),
        ) {
            Text("Delete")
        }
    }
}

@Composable
fun httpMethodField(
    endpointViewModel: EndpointViewModel
) {
    val isDropDownMenuExpanded = remember { mutableStateOf(false) }

    Column(Modifier.fillMaxWidth(0.1f).fillMaxHeight()) {
        OutlinedButton(
            onClick = { isDropDownMenuExpanded.value = true },
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        ) {
            Text("GET", fontWeight = FontWeight.Bold)
        }
        DropdownMenu(
            expanded = isDropDownMenuExpanded.value,
            onDismissRequest = { isDropDownMenuExpanded.value = false },
        ) {
            HttpMethod.entries.forEach { method ->
                DropdownMenuItem(onClick = {
                    isDropDownMenuExpanded.value = false
                }) {
                    Text(method.name)
                }
            }
        }
    }
}
