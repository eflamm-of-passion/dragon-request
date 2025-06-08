package io.eflamm.dragonrequest.ui.viewmodel

import io.eflamm.dragonrequest.ui.model.ApiFile
import io.eflamm.dragonrequest.ui.model.Collection
import io.eflamm.dragonrequest.ui.model.Endpoint
import io.eflamm.dragonrequest.ui.model.RootFile
import io.eflamm.dragonrequest.ui.model.Workspace
import io.eflamm.dragonrequest.ui.view.tree.Node

class FileTreeMapper {
    companion object {
        fun apiFileToTreeNode(
            apiFile: ApiFile,
            nextDepth: Int,
        ): Node =
            Node(
                name = apiFile.name,
                isDirectory = isDirectory(apiFile),
                children = apiFilesToTreeNodes(apiFile.getFiles(), nextDepth + 1),
                depth = nextDepth,
                value = apiFile,
            )

        private fun apiFilesToTreeNodes(
            apiFiles: List<ApiFile>,
            nextDepth: Int,
        ) = apiFiles.map { apiFileToTreeNode(it, nextDepth) }

        private fun isDirectory(apiFile: ApiFile): Boolean =
            when (apiFile) {
                is RootFile, is Workspace, is Collection -> true
                is Endpoint -> false
                else -> false
            }
    }
}
