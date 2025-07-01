package io.eflamm.dragonrequest.infrastructure.api.mapper

import io.eflamm.dragonrequest.domain.model.ApiFilename
import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.model.common.Id
import io.eflamm.dragonrequest.domain.model.endpoint.DomainName
import io.eflamm.dragonrequest.domain.model.endpoint.HttpMethod
import io.eflamm.dragonrequest.domain.model.endpoint.Path
import io.eflamm.dragonrequest.domain.model.endpoint.Port
import io.eflamm.dragonrequest.domain.model.endpoint.Protocol
import io.eflamm.dragonrequest.domain.model.endpoint.QueryParameters
import io.eflamm.dragonrequest.infrastructure.api.EndpointCreateInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EndpointMapperTest {
    @Test
    fun `GIVEN a http url with path, params and without port WHEN mapping THEN all values are set`() {
        // given
        val endpointCreateInput = EndpointCreateInput("someEndpoint", "GET", "http://acme.org/path?param=foo&more=bar", "parentId")

        // when
        val endpoint = endpointCreateInput.toEndpoint()

        // then
        assertEquals(Protocol.HTTP, endpoint.protocol)
        assertEquals("acme.org", endpoint.domain.get())
        assertEquals(80, endpoint.port.get())
        assertEquals("/path", endpoint.path.aggregate())
        assertEquals("?param=foo&more=bar", endpoint.queryParameters.aggregate())
    }

    @Test
    fun `GIVEN a https url with port and without params and path WHEN mapping THEN all values are set`() {
        // given
        val endpointCreateInput = EndpointCreateInput("someEndpoint", "GET", "https://acme.org:9091", "parentId")

        // when
        val endpoint = endpointCreateInput.toEndpoint()

        // then
        assertEquals(Protocol.HTTPS, endpoint.protocol)
        assertEquals("acme.org", endpoint.domain.get())
        assertEquals(9091, endpoint.port.get())
        assertEquals("", endpoint.path.aggregate())
        assertEquals("", endpoint.queryParameters.aggregate())
    }

    @Test
    fun `GIVEN a https url with a param only WHEN mapping THEN all values are set`() {
        // given
        val endpointCreateInput = EndpointCreateInput("someEndpoint", "GET", "https://acme.org?param=foo", "parentId")

        // when
        val endpoint = endpointCreateInput.toEndpoint()

        // then
        assertEquals(Protocol.HTTPS, endpoint.protocol)
        assertEquals("acme.org", endpoint.domain.get())
        assertEquals(443, endpoint.port.get())
        assertEquals("", endpoint.path.aggregate())
        assertEquals("?param=foo", endpoint.queryParameters.aggregate())
    }

    @Test
    fun `GIVEN a http url with a trailing slash WHEN mapping THEN all values are set`() {
        // given
        val endpointCreateInput = EndpointCreateInput("someEndpoint", "GET", "https://acme.org/", "parentId")

        // when
        val endpoint = endpointCreateInput.toEndpoint()

        // then
        assertEquals(Protocol.HTTPS, endpoint.protocol)
        assertEquals("acme.org", endpoint.domain.get())
        assertEquals(443, endpoint.port.get())
        assertEquals("", endpoint.path.aggregate())
        assertEquals("", endpoint.queryParameters.aggregate())
    }

    @Test
    fun `GIVEN an endpoint with default port, path, params WHEN THEN`() {
        // given
        val id = Id.create()
        val endpoint =
            Endpoint(
                id,
                ApiFilename("someEndpoint"),
                HttpMethod.GET,
                Protocol.HTTP,
                DomainName("acme.org"),
                Port(80),
                Path(listOf("some", "path")),
                QueryParameters(mapOf("param" to "foo", "more" to "bar")),
            )

        // when
        val endpointOutput = endpoint.toEndpointOutput()

        // then
        assertEquals(id.toString(), endpointOutput.id)
        assertEquals("someEndpoint", endpointOutput.name)
        assertEquals("http://acme.org/some/path?param=foo&more=bar", endpointOutput.url)
    }

    @Test
    fun `GIVEN an endpoint with https, port and without path, params WHEN THEN`() {
        // given
        val id = Id.create()
        val endpoint =
            Endpoint(
                id,
                ApiFilename("someEndpoint"),
                HttpMethod.GET,
                Protocol.HTTPS,
                DomainName("acme.org"),
                Port(9091),
                Path.create(),
                QueryParameters.create(),
            )

        // when
        val endpointOutput = endpoint.toEndpointOutput()

        // then
        assertEquals(id.toString(), endpointOutput.id)
        assertEquals("someEndpoint", endpointOutput.name)
        assertEquals("https://acme.org:9091", endpointOutput.url)
    }
}
