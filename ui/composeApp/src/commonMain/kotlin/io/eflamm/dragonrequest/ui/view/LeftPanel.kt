package io.eflamm.dragonrequest.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Save
import androidx.compose.runtime.Composable
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
import io.eflamm.dragonrequest.ui.view.tree.fileNode
import io.eflamm.dragonrequest.ui.viewmodel.EndpointViewModel

@Composable
fun endpointList(endpointViewModel: EndpointViewModel) {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().weight(0.9f)) {
            if (true) { // TODO check data is loaded
                Column(
                    Modifier.fillMaxWidth(1f).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                ) {
                    fileNode(endpointViewModel.loadFileTree())
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
private fun endpointListItem(
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
private fun endpointListItemOptions(
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
                TODO()
            }) {
                Text("Delete")
            }
        }
    }
}
