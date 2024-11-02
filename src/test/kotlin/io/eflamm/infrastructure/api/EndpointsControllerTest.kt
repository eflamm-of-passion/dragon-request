package io.eflamm.infrastructure.api
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import java.util.*

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
//            .body("id", equalTo(MockEndpointRepository.CREATED_ENDPOINT_UUID))
            .body("protocol", equalTo("http"))
            .body("domain", equalTo("acme.org"))
            .body("port", equalTo   (80))
            .body("path", equalTo(""))
            .body("queryParameters", equalTo(""))
    }

//    @Test
    fun getEndpointTest() {
        val expectedId = UUID.fromString("a48cd2ec-3b61-4908-b3ec-add927cd9e09").toString()

        RestAssured.given()
            .get(Constants.ENDPOINTS_URL + expectedId)
            .then()
            .statusCode(200)
//            .body("id", equalTo(expectedId))
            .body("protocol", equalTo("http"))
            .body("domain", equalTo("acme.org"))
            .body("port", equalTo(80))
            .body("path", equalTo(""))
            .body("queryParameters", equalTo(""))
    }

}