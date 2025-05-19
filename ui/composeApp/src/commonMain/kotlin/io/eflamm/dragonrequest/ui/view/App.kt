package io.eflamm.dragonrequest.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.eflamm.dragonrequest.ui.model.Endpoint
import io.eflamm.dragonrequest.ui.model.HttpMethod
import io.eflamm.dragonrequest.ui.viewmodel.EndpointViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun app(endpointViewModel: EndpointViewModel) {
    fun executeOnStart() = endpointViewModel.getEndpoints()

    MaterialTheme {
        executeOnStart()
        val endpoints by endpointViewModel.endpoints.collectAsState(initial = emptyList())
        val currentFile by endpointViewModel.currentEndpoint.collectAsState(initial = null)
        val currentEndpoint =
            if (currentFile is Endpoint) {
                currentFile as Endpoint
            } else {
                null // FIXME refacto this horror
            }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Column(Modifier.fillMaxHeight().weight(1f).background(color = Color.Cyan)) {
                endpointList(endpointViewModel, endpoints)
            }
            Column(Modifier.fillMaxHeight().weight(3f)) {
                endpointForm(endpointViewModel, currentEndpoint)
            }
        }
    }
}

@Composable
fun endpointList(
    endpointViewModel: EndpointViewModel,
    endpoints: List<Endpoint>,
) {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().weight(0.9f)) {
            if (endpoints.isNotEmpty()) {
                Column(
                    Modifier.fillMaxWidth(1f).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                ) {
                    endpoints.forEach { endpoint -> endpointListItem(endpointViewModel, endpoint) }
                }
            } else {
                Column(
                    Modifier.fillMaxWidth(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text("No endpoints saved", color = Color.Gray)
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().weight(0.1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(modifier = Modifier.fillMaxWidth(0.8f).height(48.dp), onClick = {}, colors = addButtonColors()) {
                Text("Add")
            }
        }
    }
}

@Composable
fun endpointListItem(
    endpointViewModel: EndpointViewModel,
    endpoint: Endpoint,
) {
    OutlinedButton(
        onClick = { endpointViewModel.selectFile(endpoint) },
        modifier =
            Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(top = 8.dp, start = 8.dp, end = 8.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Text(endpoint.httpMethod.name, style = TextStyle(fontWeight = FontWeight.Bold))
            // TODO add tooltip, it does not seem to be available in compose for the moment
            Text(endpoint.url, maxLines = 1, overflow = TextOverflow.Ellipsis)
            endpointListItemOptions(endpointViewModel, endpoint)
        }
    }
}

@Composable
fun endpointListItemOptions(
    endpointViewModel: EndpointViewModel,
    endpoint: Endpoint,
) {
    val isDropDownMenuExpanded = remember { mutableStateOf(false) }

    Row {
        Icon(
            Icons.Rounded.Save,
            contentDescription = "Save changes on this endpoint",
        )
        Icon(
            Icons.Rounded.MoreVert,
            contentDescription = "More options on this endpoint",
            modifier = Modifier.clickable { isDropDownMenuExpanded.value = true },
        )
        DropdownMenu(
            expanded = isDropDownMenuExpanded.value,
            onDismissRequest = { isDropDownMenuExpanded.value = false },
        ) {
            DropdownMenuItem(onClick = {
                isDropDownMenuExpanded.value = false
                endpointViewModel.deleteEndpoint(endpoint)
            }) {
                Text("Delete")
            }
        }
    }
}

@Composable
fun endpointForm(
    endpointViewModel: EndpointViewModel,
    currentEndpoint: Endpoint?,
) {
    Column(
        Modifier.fillMaxSize().background(color = Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(Modifier.fillMaxWidth(0.8f).height(50.dp)) {
            httpMethodField(endpointViewModel, currentEndpoint)
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
        Row(Modifier.fillMaxWidth(0.5f), horizontalArrangement = Arrangement.SpaceAround) {
            Button(onClick = {}, colors = sendButtonColors()) {
                Text("Send")
            }
            Button(
                onClick = { currentEndpoint?.let { endpointViewModel.saveEndpointOnRemote(it) } },
                colors = saveButtonColors(),
            ) {
                Text("Save")
            }
            Button(
                onClick = { currentEndpoint?.let { endpointViewModel.deleteEndpoint(it) } },
                colors = deleteButtonColors(),
            ) {
                Text("Delete")
            }
        }
    }
}

@Composable
fun httpMethodField(
    endpointViewModel: EndpointViewModel,
    currentEndpoint: Endpoint?,
) {
    val isDropDownMenuExpanded = remember { mutableStateOf(false) }

    Column(Modifier.fillMaxWidth(0.1f).fillMaxHeight()) {
        OutlinedButton(
            onClick = { isDropDownMenuExpanded.value = true },
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        ) {
            Text(currentEndpoint?.httpMethod?.name ?: "GET", fontWeight = FontWeight.Bold)
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

@Composable
fun sendButtonColors() = ButtonDefaults.buttonColors(backgroundColor = Color.Blue, contentColor = Color.White)

@Composable
fun saveButtonColors() = ButtonDefaults.buttonColors(backgroundColor = Color.Green, contentColor = Color.White)

@Composable
fun deleteButtonColors() = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White)

@Composable
fun addButtonColors() = ButtonDefaults.buttonColors(backgroundColor = Color.Blue, contentColor = Color.White)
