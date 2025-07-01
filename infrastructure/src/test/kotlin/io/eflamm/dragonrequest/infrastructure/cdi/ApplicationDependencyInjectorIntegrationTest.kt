package io.eflamm.dragonrequest.infrastructure.cdi

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import java.io.File

class ApplicationDependencyInjectorIntegrationTest {
    companion object {
        const val ENDPOINTS_BASE_URL = "/endpoints/"

        @JvmStatic
        @BeforeAll
        fun setup() {
            val startupArguments = listOf("integration-testing")
            ApplicationDependencyInjector().startApplication(startupArguments)
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            File("sqlite-database-testing.db").delete()
        }
    }

//    @Test
//    fun `GIVEN a url WHEN post THEN return 201 with the created endpoint`() {
//        val body = """
//            {
//                "name": "someEndpoint",
//                "httpMethod": "GET",
//                "url": "http://acme.org/path?param=foo"
//            }
//        """
//
//        RestAssured
//            .given()
//            .header("Content-Type", "application/json")
//            .body(body)
//            .post(ENDPOINTS_BASE_URL)
//            .then()
//            .statusCode(201)
//            .body("url", equalTo("http://acme.org/path?param=foo"))
//            .extract()
//            .response()
//    }
//
//    @Test
//    fun `GIVEN a malformed url WHEN post THEN return 400 with error message`() {
//        val body = """
//            {
//                "name": "someEndpoint",
//                "httpMethod": "GET",
//                "url": "http//acme.org/path?param=foo"
//            }
//        """
//
//        RestAssured
//            .given()
//            .header("Content-Type", "application/json")
//            .body(body)
//            .post(ENDPOINTS_BASE_URL)
//            .then()
//            .statusCode(400)
//            .extract()
//            .response()
//    }
//
//    @Test
//    fun `GIVEN two existing endpoints WHEN get THEN return 200 with the list of endpoints`() {
//        val firstExpectedId = createTestEndpoint()
//        val secondExpectedId = createTestEndpoint()
//
//        RestAssured
//            .given()
//            .basePath(ENDPOINTS_BASE_URL)
//            .then()
//            .statusCode(200)
//            .body("size()", equalTo(2))
//            .body("[0].id", equalTo(firstExpectedId))
//            .body("[0].url", equalTo("http://acme.org/path?param=foo"))
//            .body("[1].id", equalTo(secondExpectedId))
//            .body("[1].url", equalTo("http://acme.org/path?param=foo"))
//    }
//
//    @Test
//    fun `GIVEN an existing endpoint id WHEN get THEN return 200 with the endpoint`() {
//        val expectedId = createTestEndpoint()
//
//        RestAssured
//            .given()
//            .basePath(ENDPOINTS_BASE_URL)
//            .get(expectedId)
//            .then()
//            .statusCode(200)
//            .body("id", equalTo(expectedId))
//            .body("url", equalTo("http://acme.org/path?param=foo"))
//    }
//
//    @Test
//    fun `GIVEN an existing endpoint id WHEN get another endpoint THEN return 404`() {
//        createTestEndpoint()
//
//        RestAssured
//            .given()
//            .basePath(ENDPOINTS_BASE_URL)
//            .get(Id.create().toString())
//            .then()
//            .statusCode(404)
//    }
//
//    @Test
//    fun `GIVEN an updated existing endpoint WHEN put THEN return 200 with updated endpoint`() {
//        val expectedId = createTestEndpoint()
//        val body = """
//            {
//                "id": "$expectedId",
//                "name": "someEndpoint",
//                "httpMethod": "GET",
//                "url": "https://example.org:9091"
//            }
//        """
//
//        RestAssured
//            .given()
//            .header("Content-Type", "application/json")
//            .body(body)
//            .basePath(ENDPOINTS_BASE_URL)
//            .put(expectedId)
//            .then()
//            .statusCode(200)
//            .body("id", equalTo(expectedId))
//            .body("url", equalTo("https://example.org:9091"))
//            .extract()
//            .response()
//    }
//
//    @Test
//    fun `GIVEN an existing endpoint WHEN put on another endpoint that does not exist THEN return 404`() {
//        createTestEndpoint()
//        val someId = Id.create().toString()
//        val body = """
//            {
//                "id": "$someId",
//                "name": "someEndpoint",
//                "httpMethod": "GET",
//                "url": "https://example.org:9091"
//            }
//        """
//
//        RestAssured
//            .given()
//            .header("Content-Type", "application/json")
//            .body(body)
//            .basePath(ENDPOINTS_BASE_URL)
//            .put(someId)
//            .then()
//            .statusCode(404)
//    }
//
//    @Test
//    fun `GIVEN an updated existing endpoint WHEN put with malformed URL THEN return 400 with updated endpoint`() {
//        val expectedId = createTestEndpoint()
//        val body = """
//            {
//                "id": "$expectedId",
//                "name": "someEndpoint",
//                "httpMethod": "GET",
//                "url": "htps://example.org:9091"
//            }
//        """
//
//        RestAssured
//            .given()
//            .header("Content-Type", "application/json")
//            .body(body)
//            .basePath(ENDPOINTS_BASE_URL)
//            .put(expectedId)
//            .then()
//            .statusCode(400)
//    }
//
//    @Test
//    fun `GIVEN an existing endpoint id WHEN delete THEN return 200 `() {
//        val expectedId = createTestEndpoint()
//
//        RestAssured
//            .given()
//            .basePath(ENDPOINTS_BASE_URL)
//            .delete(expectedId)
//            .then()
//            .statusCode(200)
//    }
//
//    @Test
//    fun `GIVEN an existing endpoint id WHEN delete another endpoint THEN return 404 `() {
//        createTestEndpoint()
//
//        RestAssured
//            .given()
//            .basePath(ENDPOINTS_BASE_URL)
//            .delete(Id.create().toString())
//            .then()
//            .statusCode(404)
//    }
//
//    private fun createTestEndpoint(): String {
//        val endpointJson = """
//            {
//                "name": "someEndpoint",
//                "httpMethod": "GET",
//                "url": "http://acme.org/path?param=foo"
//            }
//        """
//
//        val response =
//            RestAssured
//                .given()
//                .contentType(ContentType.JSON)
//                .body(endpointJson)
//                .post(ENDPOINTS_BASE_URL)
//                .then()
//                .statusCode(201)
//                .extract()
//                .response()
//
//        return response.jsonPath().getString("id")
//    }
}
