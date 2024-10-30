package io.eflamm.infrastructure.persistence

import io.eflamm.infrastructure.EndpointUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SqliteRepositoryTest {

    private lateinit var repository: SqliteRepository

    @BeforeAll
    fun setupDatabase() {
        repository = SqliteRepository(":memory:")
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
    fun `GIVEN an enpoint WHEN create AND get this enpoint THEN returns the created endpoint `() {
        // given
        val endpointToCreate = EndpointUtils.createEndpointWithoutId()

        // when
        val createdEndpoint = repository.createEndpoint(endpointToCreate)

        // then
        assertThat(endpointToCreate)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(createdEndpoint)
    }

    @Test
    fun `GIVEN an endpoint in the database WHEN updating the endpoint THEN the endpoint is updated in database`() {
        fail("Not yet implemented")
    }

    @Test
    fun `GIVEN an endpoint in the database WHEN updating an endpoint that do not exist THEN it throws an exception`() {
        fail("Not yet implemented")
    }

    @Test
    fun `GIVEN an endpoint in the database WHEN deleting an endpoint THEN the endpoint deleted in database`() {
        // given
        val endpointToCreate = EndpointUtils.createEndpointWithoutId()
        val endpointToDelete = repository.createEndpoint(endpointToCreate)

        // when
        repository.deleteEndpoint(endpointToDelete.id!!)
        val endpointDeleted = repository.getEndpoint(endpointToDelete.id!!)

        // then
        assertThat(endpointDeleted).isNull()

    }

    @Test
    fun `GIVEN an endpoint in the database WHEN deleting another endpoint THEN it throws an exception`() {
        fail("Not yet implemented")
    }
}