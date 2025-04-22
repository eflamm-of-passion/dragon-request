package io.eflamm.dragonrequest.ui.model

import kotlin.test.Test

class ApiFileTest {
    // TODO test each kind of combination of a collection, initiated endpoint, saved endpoint
    // TODO test the manipulation with the list

    @Test
    fun `GIVEN nothing WHEN initiate a collection THEN all the fields are coherent`() {
        // given
        val workspace = Workspace("workspace")
        val collection = Collection("collection")
        val initiatedEndpoint = Endpoint("initial id", EndpointState.Initiated, "Initial endpoint", HttpMethod.GET, "google.com")
        val savedEndpoint = Endpoint("saved id", EndpointState.Initiated, "Saved endpoint", HttpMethod.GET, "youtube.com")

        // when
        workspace.addFile(collection)
        collection.addFile(initiatedEndpoint)
        workspace.addFile(savedEndpoint)

        // then
    }
}
