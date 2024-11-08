package io.eflamm.infrastructure.api
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import jakarta.ws.rs.core.Response
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test

@QuarkusTest
class EndpointsControllerTest {

    object Constants {
        const val ENDPOINTS_URL = "/endpoints/"
    }

    @Test
    fun createEndpointTest() {
        val body = """
            {
                "url": "http://acme.org/path?param=foo"
            }
        """

        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(body)
            .post(Constants.ENDPOINTS_URL)
            .then()
            .statusCode(201)
            .body("url", equalTo("http://acme.org/path?param=foo"))
            .extract().response()
    }

    @Test
    fun getEndpointTest() {
        val expectedId = createTestEndpoint()

        RestAssured.given()
            .basePath(Constants.ENDPOINTS_URL)
            .get(expectedId)
            .then()
            .statusCode(200)
            .body("id", equalTo(expectedId))
            .body("url", equalTo("http://acme.org/path?param=foo"))
    }

    @Test
    fun updateEndpointTest() {
        val expectedId = createTestEndpoint()
        val body = """
            {
                "id": "$expectedId",
                "url": "https://example.org:9091"
            }
        """

        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(body)
            .put(Constants.ENDPOINTS_URL)
            .then()
            .statusCode(200)
            .body("id", equalTo(expectedId))
            .body("url", equalTo("https://example.org:9091"))
            .extract().response()
    }

    @Test
    fun deleteEndpointTest() {
        val expectedId = createTestEndpoint()

        RestAssured.given()
            .basePath(Constants.ENDPOINTS_URL)
            .delete(expectedId)
            .then()
            .statusCode(200)
    }

    private fun createTestEndpoint(): String? {
        val endpointJson = """
            {
                "url": "http://acme.org/path?param=foo"
            }
        """

        val response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(endpointJson)
            .post(Constants.ENDPOINTS_URL)
            .then()
            .statusCode(Response.Status.CREATED.statusCode)
            .extract()
            .response()

        return response.jsonPath().getString("id")
    }

}