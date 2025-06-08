package io.eflamm.dragonrequest.ui.view.tree

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.eflamm.dragonrequest.ui.model.ApiFile

data class Node(
    val name: String,
    val isDirectory: Boolean,
    val children: List<Node> = emptyList(),
    val depth: Int,
    val value: ApiFile,
)

@Composable
fun fileNode(node: Node) {
    val isRootNode = node.depth < 1
    val isClicked = remember { mutableStateOf(isRootNode) }

    Column(Modifier.padding(start = nodePadding(node.depth).dp).clickable { isClicked.value = !isClicked.value }) {
        currentNode(!isRootNode, isClicked.value, node)
        nodeChildren(isClicked.value, node.children)
        emptyDirectory(isClicked.value && node.isDirectory && node.children.isEmpty(), node.depth + 1)
    }
}

private fun nodePadding(depth: Int) =
    if (depth > 0) {
        (depth - 1) * 16
    } else {
        0
    }

@Composable
fun currentNode(
    isDisplayed: Boolean,
    isClicked: Boolean,
    node: Node,
) = if (isDisplayed) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        nodeArrow(node.isDirectory, isClicked)
        Text(text = node.name, fontSize = 16.sp)
    }
}

@Composable
fun nodeChildren(
    isExpanded: Boolean,
    children: List<Node>,
) {
    if (isExpanded) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            children.forEach {
                fileNode(it)
            }
        }
    }
}

@Composable
fun emptyDirectory(
    isDisplayed: Boolean,
    depth: Int,
) {
    if (isDisplayed) {
        Column(Modifier.padding(start = (depth * 16).dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Empty directory", fontSize = 16.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun nodeArrow(
    isDisplayed: Boolean,
    isClicked: Boolean,
) {
    if (isDisplayed) {
        val rotationAngle = animateFloatAsState(if (isClicked) 90f else 0f).value
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Expand/Collapse",
            modifier =
                Modifier
                    .graphicsLayer(rotationZ = rotationAngle)
                    .size(24.dp),
        )
    } else {
        Spacer(modifier = Modifier.size(24.dp))
    }
}
