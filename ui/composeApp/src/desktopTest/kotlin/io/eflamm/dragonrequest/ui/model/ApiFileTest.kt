package io.eflamm.dragonrequest.ui.model

import io.eflamm.dragonrequest.ui.model.states.Initiated
import kotlin.test.Test

class ApiFileTest {
    // TODO test each kind of combination of a collection, initiated endpoint, saved endpoint
    // TODO test the manipulation with the list

    @Test
    fun `GIVEN nothing WHEN initiate a collection THEN all the fields are coherent`() {
        // given
        val workspace = Workspace(name = "workspace")
        val collection = Collection(name = "collection")
        val initiatedEndpoint =
            Endpoint(
                id = "initial id",
                state = Initiated(),
                name = "Initial endpoint",
                httpMethod = HttpMethod.GET,
                url = "google.com",
            )
        val savedEndpoint =
            Endpoint(
                id = "saved id",
                state = Initiated(),
                name = "Saved endpoint",
                httpMethod = HttpMethod.GET,
                url = "youtube.com",
            )

        // when
        workspace.addFile(collection)
        collection.addFile(initiatedEndpoint)
        workspace.addFile(savedEndpoint)

        // then
    }
}
