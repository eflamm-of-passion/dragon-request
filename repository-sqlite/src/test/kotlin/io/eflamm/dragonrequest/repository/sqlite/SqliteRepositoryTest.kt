package io.eflamm.dragonrequest.repository.sqlite

import io.eflamm.dragonrequest.domain.exception.EndpointException
import io.eflamm.dragonrequest.domain.exception.ErrorType
import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.model.endpoint.*
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SqliteRepositoryTest {

    private lateinit var repository: SqliteRepository

    // TODO test with an SQLException

    private fun createEndpointWitRandomId(): Endpoint = Endpoint(
        Id.create(),
        "someEndpoint",
        HttpMethod.GET,
        Protocol.HTTP,
        DomainName("acme.org"),
        Port(80),
        Path(listOf("path", "more")),
        QueryParameters(mapOf("one" to "foo", "two" to "bar"))
    )

    @BeforeAll
    fun setupDatabase() {
        repository = SqliteRepository(":memory:", mockk(relaxed = true))
        repository.connect()
    }

    @AfterAll
    fun tearDownDatabase() {
        repository.clearDatabase()
        repository.disconnect()
    }

    @AfterEach
    fun cleanDatabase() {
        repository.cleanDatabase()
    }

    @Test
    fun `GIVEN an endpoint WHEN create AND get this endpoint THEN returns the created endpoint `() {
        // given
        val endpointToCreate = createEndpointWitRandomId()

        // when
        val createdEndpointResult = repository.createEndpoint(endpointToCreate)
        val createdEndpoint = createdEndpointResult.getOrNull()

        // then
        assertThat((createdEndpointResult.isSuccess)).isTrue()
        assertThat(endpointToCreate)
            .usingRecursiveComparison()
            .isEqualTo(createdEndpoint)
    }

    @Test
    fun `GIVEN two endpoints WHEN getting endpoints THEN returns a list with both endpoints`() {
        // given
        val endpointOneToCreate = createEndpointWitRandomId()
        val endpointTwoToCreate = createEndpointWitRandomId()

        // when
        repository.createEndpoint(endpointOneToCreate)
        repository.createEndpoint(endpointTwoToCreate)
        val getEndpointResult = repository.getEndpoints()

        // then
        assertThat((getEndpointResult.isSuccess)).isTrue()
        assertThat(getEndpointResult.getOrNull()?.get(0))
            .usingRecursiveComparison()
            .isEqualTo(endpointOneToCreate)
        assertThat(getEndpointResult.getOrNull()?.get(1))
            .usingRecursiveComparison()
            .isEqualTo(endpointTwoToCreate)
    }

    @Test
    fun `GIVEN no endpoints WHEN get endpoints THEN returns an empty list`() {
        // given
        val endpointOneToCreate = createEndpointWitRandomId()
        val endpointTwoToCreate = createEndpointWitRandomId()

        // when
        repository.createEndpoint(endpointOneToCreate)
        repository.createEndpoint(endpointTwoToCreate)
        val getEndpointResult = repository.getEndpoints()

        // then
        assertThat((getEndpointResult.isSuccess)).isTrue()
        assertThat(getEndpointResult.getOrNull()?.isEmpty())
    }

    @Test
    fun `GIVEN and endpoint WHEN get another endpoint THEN returns failure`() {
        // given
        val endpointToCreate = createEndpointWitRandomId()

        // when
        repository.createEndpoint(endpointToCreate)
        val getEndpointResult = repository.getEndpoint(Id.create())

        // then
        assertThat((getEndpointResult.isFailure)).isTrue()
        assertThat(getEndpointResult.exceptionOrNull()).isInstanceOf(EndpointException::class.java)
        assertThat((getEndpointResult.exceptionOrNull() as EndpointException).type).isEqualTo(ErrorType.ENTITY_NOT_FOUND)
    }

    @Test
    @Disabled
    fun `GIVEN a repository that cannot connect WHEN get endpoint THEN returns failure`() {
        // TODO make it work
    }

    @Test
    fun `GIVEN an endpoint in the database WHEN updating the endpoint THEN the endpoint is updated in database`() {
        // given
        val earlierCreatedEndpoint = repository.createEndpoint(createEndpointWitRandomId()).getOrNull()!!
        val endpointToUpdate = Endpoint(
            earlierCreatedEndpoint.id,
            "someEndpoint",
            HttpMethod.GET,
            Protocol.HTTPS,
            DomainName("other-domain.com"),
            Port(8080),
            earlierCreatedEndpoint.path,
            earlierCreatedEndpoint.queryParameters
        )

        // when
        val endpointUpdatedResult = repository.updateEndpoint(endpointToUpdate)
        val endpointUpdated = endpointUpdatedResult.getOrNull()

        // then
        assertThat((endpointUpdatedResult.isSuccess)).isTrue()
        assertThat(endpointUpdated)
            .usingRecursiveComparison()
            .isEqualTo(endpointToUpdate)
    }

    @Test
    fun `GIVEN an endpoint in the database WHEN updating an endpoint that do not exist THEN it returns failure`() {
        // given
        val earlierCreatedEndpoint = repository.createEndpoint(createEndpointWitRandomId()).getOrNull()!!
        val endpointToUpdate = Endpoint(
            Id.create(),
            "someEndpoint",
            HttpMethod.GET,
            Protocol.HTTPS,
            DomainName("other-domain.com"),
            Port(8080),
            earlierCreatedEndpoint.path,
            earlierCreatedEndpoint.queryParameters
        )

        // when
        val endpointUpdatedResult = repository.updateEndpoint(endpointToUpdate)

        // then
        assertThat((endpointUpdatedResult.isFailure)).isTrue()
        assertThat(endpointUpdatedResult.exceptionOrNull()).isInstanceOf(EndpointException::class.java)
        assertThat((endpointUpdatedResult.exceptionOrNull() as EndpointException).type).isEqualTo(ErrorType.ENTITY_NOT_FOUND)
    }

    @Test
    fun `GIVEN an endpoint in the database WHEN deleting an endpoint THEN the endpoint deleted in database`() {
        // given
        val endpointToCreate = createEndpointWitRandomId()
        val endpointToDelete = repository.createEndpoint(endpointToCreate).getOrNull()!!

        // when
        val endpointDeleteResult = repository.deleteEndpoint(endpointToDelete.id)
        val endpointDeleted = repository.getEndpoint(endpointToDelete.id).getOrNull()

        // then
        assertThat(endpointDeleteResult.isSuccess).isTrue()
        assertThat(endpointDeleted).isNull()
    }

    @Test
    fun `GIVEN an endpoint in the database WHEN deleting another endpoint THEN it throws an exception`() {
        // given
        val endpointToCreate = createEndpointWitRandomId()
        repository.createEndpoint(endpointToCreate).getOrNull()!!

        // when
        val endpointDeleteResult = repository.deleteEndpoint(Id.create())

        // then
        assertThat(endpointDeleteResult.isFailure).isTrue()
        assertThat(endpointDeleteResult.exceptionOrNull()).isInstanceOf(EndpointException::class.java)
    }
}
