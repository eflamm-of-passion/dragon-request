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
                "protocol": "http",
                "domain": "acme.org",
                "port": 80,
                "path": "/",
                "queryParameters": ""
            }
        """

        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(body)
            .post(Constants.ENDPOINTS_URL)
            .then()
            .statusCode(201)
            .body("protocol", equalTo("http"))
            .body("domain", equalTo("acme.org"))
            .body("port", equalTo   (80))
            .body("path", equalTo(""))
            .body("queryParameters", equalTo(""))
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
            .body("protocol", equalTo("http"))
            .body("domain", equalTo("acme.org"))
            .body("port", equalTo(80))
            .body("path", equalTo(""))
            .body("queryParameters", equalTo(""))
    }

    @Test
    fun updateEndpointTest() {
        val expectedId = createTestEndpoint()
        val body = """
            {
                "id": "$expectedId",
                "protocol": "https",
                "domain": "example.org",
                "port": 8080,
                "path": "/",
                "queryParameters": ""
            }
        """

        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(body)
            .put(Constants.ENDPOINTS_URL)
            .then()
            .statusCode(200)
            .body("id", equalTo(expectedId))
            .body("protocol", equalTo("https"))
            .body("domain", equalTo("example.org"))
            .body("port", equalTo   (8080))
            .body("path", equalTo(""))
            .body("queryParameters", equalTo(""))
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
                "protocol": "http",
                "domain": "acme.org",
                "port": 80,
                "path": "/",
                "queryParameters": ""
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