package io.eflamm.dragonrequest.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.eflamm.dragonrequest.ui.model.Endpoint
import io.eflamm.dragonrequest.ui.viewmodel.EndpointViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App(endpointViewModel: EndpointViewModel) {
    fun executeOnStart() {
        endpointViewModel.loadEndpoints()
    }

    MaterialTheme {
        executeOnStart()
        val endpoints = endpointViewModel.endpoints
        val (currentEndpoint, setCurrentEndpoint) = remember { mutableStateOf("https://www.google.com") }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Column(Modifier.fillMaxHeight().weight(1f).background(color = Color.Cyan)) {
                EndpointList(endpoints)
            }
            Column(Modifier.fillMaxHeight().weight(3f)) {
                EndpointForm(currentEndpoint, setCurrentEndpoint)
            }
        }
    }
}

@Composable
fun EndpointList(endpoints: List<Endpoint>) {
    if (endpoints.isNotEmpty()) {
        Column(Modifier.fillMaxWidth(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Top) {
            endpoints.forEach { EndpointListItem(it) }
        }
    } else {
        Column(Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("No endpoints saved", color = Color.Gray)
        }
    }
}

@Composable
fun EndpointListItem(endpoint: Endpoint) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(endpoint.httpMethod, style = TextStyle(fontWeight = FontWeight.Bold))
        // TODO add tooltip, it does not seem to be available in compose for the moment
        Text(endpoint.url, maxLines = 1, overflow = TextOverflow.Ellipsis)
        EndpointListItemOptions()
    }
}

@Composable
fun EndpointListItemOptions() {
    Row {
        Icon(
            Icons.Rounded.Save,
            contentDescription = "Save changes on this endpoint"
        )
        Icon(
            Icons.Rounded.MoreVert,
            contentDescription = "More options on this endpoint"
        )
    }
}

@Composable
fun EndpointForm(currentEndpoint: String, setCurrentEndpoint: (String) -> Unit) {
    Column(
        Modifier.fillMaxSize().background(color = Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(0.8f)) {
            TextField(
                value = currentEndpoint,
                onValueChange = setCurrentEndpoint,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White)
            )
        }
        Row(Modifier.fillMaxWidth(0.5f), horizontalArrangement = Arrangement.SpaceAround) {
            Button(onClick = {}, colors = SendButtonColors()) {
                Text("Send")
            }
            Button(onClick = {}, colors = SaveButtonColors()) {
                Text("Save")
            }
            Button(onClick = {}, colors = DeleteButtonColors()) {
                Text("Delete")
            }
        }
    }
}

@Composable
fun SendButtonColors() = ButtonDefaults.buttonColors(backgroundColor = Color.Blue, contentColor = Color.White)

@Composable
fun SaveButtonColors() = ButtonDefaults.buttonColors(backgroundColor = Color.Green, contentColor = Color.White)

@Composable
fun DeleteButtonColors() = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White)