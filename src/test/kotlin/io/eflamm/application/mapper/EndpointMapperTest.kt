package io.eflamm.application.mapper

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.*
import io.eflamm.infrastructure.api.EndpointCreateInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EndpointMapperTest {

    @Test
    fun `GIVEN a http url with path, params and without port WHEN mapping THEN all values are set`(){
        // given
        val endpointCreateInput = EndpointCreateInput("http://acme.org/path?param=foo&more=bar")

        // when
        val endpoint = EndpointMapper.dtoToBusiness(endpointCreateInput)

        // then
        assertEquals(Protocol.HTTP, endpoint.protocol)
        assertEquals("acme.org", endpoint.domain.get())
        assertEquals(80, endpoint.port.get())
        assertEquals("/path", endpoint.path.aggregate())
        assertEquals("?param=foo&more=bar", endpoint.queryParameters.aggregate())
    }

    @Test
    fun `GIVEN a https url with port and without params and path WHEN mapping THEN all values are set`(){
        // given
        val endpointCreateInput = EndpointCreateInput("https://acme.org:9091")

        // when
        val endpoint = EndpointMapper.dtoToBusiness(endpointCreateInput)

        // then
        assertEquals(Protocol.HTTPS, endpoint.protocol)
        assertEquals("acme.org", endpoint.domain.get())
        assertEquals(9091, endpoint.port.get())
        assertEquals("", endpoint.path.aggregate())
        assertEquals("", endpoint.queryParameters.aggregate())
    }

    @Test
    fun `GIVEN a https url with a param only WHEN mapping THEN all values are set`(){
        // given
        val endpointCreateInput = EndpointCreateInput("https://acme.org?param=foo")

        // when
        val endpoint = EndpointMapper.dtoToBusiness(endpointCreateInput)

        // then
        assertEquals(Protocol.HTTPS, endpoint.protocol)
        assertEquals("acme.org", endpoint.domain.get())
        assertEquals(443, endpoint.port.get())
        assertEquals("", endpoint.path.aggregate())
        assertEquals("?param=foo", endpoint.queryParameters.aggregate())
    }

    @Test
    fun `GIVEN a http url with a trailing slash WHEN mapping THEN all values are set`(){
        // given
        val endpointCreateInput = EndpointCreateInput("https://acme.org/")

        // when
        val endpoint = EndpointMapper.dtoToBusiness(endpointCreateInput)

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
        val endpoint = Endpoint(
            id,
            Protocol.HTTP,
            DomainName("acme.org"),
            Port(80),
            Path(listOf("some", "path")),
            QueryParameters(mapOf("param" to "foo", "more" to "bar"))
        )

        // when
        val endpointOutput = EndpointMapper.businessToDto(endpoint)

        // then
        assertEquals(id.get(), endpointOutput.id)
        assertEquals("http://acme.org/some/path?param=foo&more=bar", endpointOutput.url)
    }

    @Test
    fun `GIVEN an endpoint with https, port and without path, params WHEN THEN`() {
        // given
        val id = Id.create()
        val endpoint = Endpoint(
            id,
            Protocol.HTTPS,
            DomainName("acme.org"),
            Port(9091),
            Path.create(),
            QueryParameters.create()
        )

        // when
        val endpointOutput = EndpointMapper.businessToDto(endpoint)

        // then
        assertEquals(id.get(), endpointOutput.id)
        assertEquals("https://acme.org:9091", endpointOutput.url)
    }
}