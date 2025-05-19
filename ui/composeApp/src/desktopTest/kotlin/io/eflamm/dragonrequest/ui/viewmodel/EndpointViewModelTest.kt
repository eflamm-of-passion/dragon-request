package io.eflamm.dragonrequest.ui.viewmodel

import io.eflamm.dragonrequest.ui.model.Endpoint
import io.eflamm.dragonrequest.ui.model.HttpMethod
import io.eflamm.dragonrequest.ui.model.RootFile
import io.eflamm.dragonrequest.ui.model.Workspace
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class EndpointViewModelTest {
    @Test
    fun `GIVEN nothing WHEN startup the app THEN a new workspace is created with a new initiated endpoint that is selected`() {
        // note : no api file is orphaned
        // given
        val endpointProvider: EndpointProvider = mockk()
        val endpointViewModel = EndpointViewModel(endpointProvider)

        // when
        endpointViewModel.initEndpointFromRoot()
        val selectedEndpoint = endpointViewModel.currentEndpoint.value as Endpoint
        val defaultWorkspace = selectedEndpoint.parent

        // then
        // the parent of the initiated endpoint should be the default workspace
        assertThat(defaultWorkspace).isInstanceOf(Workspace::class.java)
        defaultWorkspace as Workspace
        assertThat(defaultWorkspace.name).isEqualTo("My workspace")

        // the endpoint should be initiated with the expected values
        assertThat(selectedEndpoint.id).isEqualTo("")
        assertThat(selectedEndpoint.name).isEqualTo("New endpoint")
        assertThat(selectedEndpoint.httpMethod).isEqualTo(HttpMethod.GET)
        assertThat(selectedEndpoint.url).isEqualTo("https://example.org")

        // the default workspace should have the initiated endpoint in its children
        assertThat(defaultWorkspace.children.size).isEqualTo(1)
        assertThat(defaultWorkspace.children.first()).isEqualTo(selectedEndpoint)

        // the root file has the default workspace in its children
        assertThat(RootFile.children.first()).isEqualTo(defaultWorkspace)
    }

    @Test
    fun `GIVEN app is initiated WHEN creating a new workspace THEN the workspace is created`() {
        // given
        val endpointProvider: EndpointProvider = mockk()
        val endpointViewModel = EndpointViewModel(endpointProvider)
        val newWorkspace = Workspace("new workspace")

        // when
        // TODO probably I will add Strategy
        // TODO add a State to the Workspace
        

        // then
    }
}
