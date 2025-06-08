package io.eflamm.dragonrequest.ui.viewmodel

import androidx.lifecycle.ViewModel
import io.eflamm.dragonrequest.ui.model.ApiFile
import io.eflamm.dragonrequest.ui.model.Endpoint
import io.eflamm.dragonrequest.ui.model.RootFile
import io.eflamm.dragonrequest.ui.model.Workspace
import io.eflamm.dragonrequest.ui.view.tree.Node
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EndpointViewModel(
    private val endpointProvider: EndpointProvider,
) : ViewModel() {
    val rootFile = RootFile
    private lateinit var defaultWorkspace: Workspace

    private val _selectedFile = MutableStateFlow<ApiFile?>(null)
    val selectedFile: StateFlow<ApiFile?> = _selectedFile.asStateFlow()

    init {
        if (false) {
            // TODO check if data, then load
        } else {
            firstStartup()
        }
    }

    private fun firstStartup() {
        // UC : the user starts the app for the first time
        this.initAndSelectWorkspace("My workspace")
        this.defaultWorkspace = _selectedFile.value as Workspace
        this.initEndpointFromDefaultWorkspace()
    }

    fun selectFile(selectedFile: ApiFile) {
        // UC : the user clicks on a file in the list
        _selectedFile.value = selectedFile
    }

    fun initAndSelectFile(parentFile: ApiFile) {
        // TODO I wonder how I could pass the type to create as input

        // UC : the user creates an endpoint from a selected file
        // TODO i should put as argument which class I want to init
        val initiatedEndpoint = Endpoint()
        // TODO try to add to parent, handle the result
        parentFile.addFile(initiatedEndpoint)
        selectFile(initiatedEndpoint)
    }

    fun initEndpointFromDefaultWorkspace() = initAndSelectFile(defaultWorkspace) // UC : the user click on Add an Endpoint

    fun initAndSelectWorkspace(workspaceName: String) {
        // UC : the clicks on the button to create a new workspace
        val initiatedWorkspace = Workspace(workspaceName)
        RootFile.addFile(initiatedWorkspace)
        selectFile(initiatedWorkspace)
    }

    // UC : the file tree is displayed after the data is loaded, and each time it is updated
    fun loadFileTree(): Node = FileTreeMapper.apiFileToTreeNode(rootFile, 0)
}
