package io.eflamm.dragonrequest.ui.viewmodel

import io.eflamm.dragonrequest.ui.model.Collection
import io.eflamm.dragonrequest.ui.model.Endpoint
import io.eflamm.dragonrequest.ui.model.RootFile
import io.eflamm.dragonrequest.ui.model.Workspace
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import kotlin.test.Test

class FileTreeMapperTest {
    @After
    fun tearDown() {
        RootFile.removeAllFiles()
    }

    @Test
    fun `GIVEN root file with no children WHEN mapping to tree THEN return empty root node`() {
        // given
        val rootFile = RootFile

        // when
        val result = FileTreeMapper.apiFileToTreeNode(rootFile, 0)

        // then
        assertThat(result.name).isEqualTo("Root")
        assertThat(result.isDirectory).isTrue()
        assertThat(result.depth).isEqualTo(0)
        assertThat(result.children).isEmpty()
    }

    @Test
    fun `GIVEN root with workspace containing collection and endpoint WHEN mapping to tree THEN return correct hierarchy`() {
        // given
        val workspace = Workspace("My workspace")
        val collection = Collection("My collection")
        val endpoint = Endpoint()

        RootFile.addFile(workspace)
        workspace.addFile(collection)
        collection.addFile(endpoint)

        // when
        val result = FileTreeMapper.apiFileToTreeNode(RootFile, 0)

        // then
        assertThat(result.name).isEqualTo("Root")
        assertThat(result.isDirectory).isTrue()
        assertThat(result.children).hasSize(1)

        val workspaceNode = result.children[0]
        assertThat(workspaceNode.name).isEqualTo("My workspace")
        assertThat(workspaceNode.depth).isEqualTo(1)
        assertThat(workspaceNode.children).hasSize(1)

        val collectionNode = workspaceNode.children[0]
        assertThat(collectionNode.name).isEqualTo("My collection")
        assertThat(collectionNode.depth).isEqualTo(2)
        assertThat(collectionNode.children).hasSize(1)

        val endpointNode = collectionNode.children[0]
        assertThat(endpointNode.name).isEqualTo("New endpoint")
        assertThat(endpointNode.depth).isEqualTo(3)
        assertThat(endpointNode.children).isEmpty()
    }

    @Test
    fun `GIVEN root with workspace containing collection and separate endpoint WHEN mapping to tree THEN return correct hierarchy`() {
        // given
        val workspace = Workspace("My workspace")
        val collection = Collection("My collection")
        val endpoint = Endpoint()

        RootFile.addFile(workspace)
        workspace.addFile(collection)
        workspace.addFile(endpoint)

        // when
        val result = FileTreeMapper.apiFileToTreeNode(RootFile, 0)

        // then
        assertThat(result.name).isEqualTo("Root")
        assertThat(result.isDirectory).isTrue()
        assertThat(result.children).hasSize(1)

        val workspaceNode = result.children[0]
        assertThat(workspaceNode.name).isEqualTo("My workspace")
        assertThat(workspaceNode.depth).isEqualTo(1)
        assertThat(workspaceNode.children).hasSize(2)

        val collectionNode = workspaceNode.children[0]
        assertThat(collectionNode.name).isEqualTo("My collection")
        assertThat(collectionNode.depth).isEqualTo(2)
        assertThat(collectionNode.children).isEmpty()

        val endpointNode = workspaceNode.children[1]
        assertThat(endpointNode.name).isEqualTo("New endpoint")
        assertThat(endpointNode.depth).isEqualTo(2)
        assertThat(endpointNode.children).isEmpty()
    }
}
