package io.eflamm.dragonrequest.repository.mongodb

import io.eflamm.dragonrequest.domain.model.ApiFilename
import io.eflamm.dragonrequest.domain.model.Collection
import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.model.Workspace
import io.eflamm.dragonrequest.domain.model.common.Id
import io.eflamm.dragonrequest.domain.model.endpoint.DomainName
import io.eflamm.dragonrequest.domain.model.endpoint.HttpMethod
import io.eflamm.dragonrequest.domain.model.endpoint.Path
import io.eflamm.dragonrequest.domain.model.endpoint.Port
import io.eflamm.dragonrequest.domain.model.endpoint.Protocol
import io.eflamm.dragonrequest.domain.model.endpoint.QueryParameters
import io.eflamm.dragonrequest.domain.monitoring.Logger
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.bson.Document
import org.junit.jupiter.api.Test
import java.util.UUID

class MongoRepositoryTest {
    private val mongoConnector: MongoConnector = mockk()
    private val logger = mockk<Logger>(relaxed = true)
    private val repository: MongoRepository = MongoRepository(mongoConnector, logger)

    // TODO test with an MongoException or something like that

    private fun createEndpointWitRandomId(): Endpoint =
        Endpoint(
            Id.create(),
            ApiFilename("someEndpoint"),
            HttpMethod.GET,
            Protocol.HTTP,
            DomainName("acme.org"),
            Port(80),
            Path(listOf("path", "more")),
            QueryParameters(mapOf("one" to "foo", "two" to "bar")),
        )

    fun generateId() = Id.fromString(UUID.randomUUID().toString())

    @Test
    fun `GIVEN a list of document with a workspace and a collection and an endpoint WHEN mapping document to business THEN returns a tree of api files`() {
        // given
        val endpointId = generateId()
        val collectionId = generateId()
        val workspaceId = generateId()
        val endpointDocument =
            Document()
                .append("type", "endpoint")
                .append("parentId", collectionId.toString())
                .append("id", endpointId.toString())
                .append("name", "some endpoint")
                .append("httpMethod", "GET")
                .append("protocol", "HTTP")
                .append("domain", "example.org")
                .append("path", "/")
        val collectionDocument =
            Document()
                .append("type", "collection")
                .append("parentId", workspaceId.toString())
                .append("id", collectionId.toString())
                .append("name", "some collection")
        val workspaceDocument =
            Document()
                .append("type", "workspace")
                .append("id", workspaceId.toString())
                .append("name", "some workspace")

        val listOfDocuments = mutableListOf<Document>()
        listOfDocuments.add(collectionDocument)
        listOfDocuments.add(endpointDocument)
        listOfDocuments.add(workspaceDocument)

        every { mongoConnector.find() } returns listOfDocuments.toList()

        // when
        val actualListOfWorkspaces = repository.getAllApiFiles().getOrNull()!!
        val actualWorkspaceFile = actualListOfWorkspaces.first()
        val actualCollectionFile = actualWorkspaceFile.getFiles().first()
        val actualEndpointFile = actualCollectionFile.getFiles().first()

        // then
        assertThat(actualWorkspaceFile).isInstanceOf(Workspace::class.java)
        assertThat(actualWorkspaceFile.name.value).isEqualTo("some workspace")
        assertThat(actualCollectionFile).isInstanceOf(Collection::class.java)
        assertThat(actualCollectionFile.name.value).isEqualTo("some collection")
        assertThat(actualEndpointFile).isInstanceOf(Endpoint::class.java)
        assertThat(actualEndpointFile.name.value).isEqualTo("some endpoint")
    }
}
