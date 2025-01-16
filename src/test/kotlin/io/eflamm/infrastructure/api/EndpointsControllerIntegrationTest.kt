package io.eflamm.infrastructure.api
//
//import io.eflamm.dragonrequest.domain.model.endpoint.Id
//import io.quarkus.test.junit.QuarkusTest
//import io.restassured.RestAssured
//import io.restassured.http.ContentType
//import jakarta.ws.rs.core.Response
//import org.hamcrest.CoreMatchers.equalTo
//import org.junit.jupiter.api.Test
//
//@QuarkusTest
class EndpointsControllerIntegrationTest {
//
//    object Constants {
//        const val ENDPOINTS_BASE_URL = "/endpoints/"
//    }
//
//    // TODO get an endpoint but the repository is not connected
//
//    @Test
//    fun `GIVEN a url WHEN post THEN return 201 with the created endpoint`() {
//        val body = """
//            {
//                "url": "http://acme.org/path?param=foo"
//            }
//        """
//
//        RestAssured.given()
//            .header("Content-Type", "application/json")
//            .body(body)
//            .post(Constants.ENDPOINTS_BASE_URL)
//            .then()
//            .statusCode(201)
//            .body("url", equalTo("http://acme.org/path?param=foo"))
//            .extract().response()
//    }
//
//    @Test
//    fun `GIVEN a malformed url WHEN post THEN return 400 with error message`() {
//        val body = """
//            {
//                "url": "http//acme.org/path?param=foo"
//            }
//        """
//
//        RestAssured.given()
//            .header("Content-Type", "application/json")
//            .body(body)
//            .post(Constants.ENDPOINTS_BASE_URL)
//            .then()
//            .statusCode(400)
//            .extract().response()
//    }
//
//    @Test
//    fun `GIVEN two existing endpoints WHEN get THEN return 200 with the list of endpoints`() {
//        val firstExpectedId = createTestEndpoint()
//        val secondExpectedId = createTestEndpoint()
//
//        RestAssured.given()
//            .basePath(Constants.ENDPOINTS_BASE_URL)
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
//        RestAssured.given()
//            .basePath(Constants.ENDPOINTS_BASE_URL)
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
//        RestAssured.given()
//            .basePath(Constants.ENDPOINTS_BASE_URL)
//            .get(Id.create().get())
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
//                "url": "https://example.org:9091"
//            }
//        """
//
//        RestAssured.given()
//            .header("Content-Type", "application/json")
//            .body(body)
//            .basePath(Constants.ENDPOINTS_BASE_URL)
//            .put(expectedId)
//            .then()
//            .statusCode(200)
//            .body("id", equalTo(expectedId))
//            .body("url", equalTo("https://example.org:9091"))
//            .extract().response()
//    }
//
//    @Test
//    fun `GIVEN an existing endpoint WHEN put on another endpoint that does not exist THEN return 404`() {
//        createTestEndpoint()
//        val someId = Id.create().get()
//        val body = """
//            {
//                "id": "$someId",
//                "url": "https://example.org:9091"
//            }
//        """
//
//        RestAssured.given()
//            .header("Content-Type", "application/json")
//            .body(body)
//            .basePath(Constants.ENDPOINTS_BASE_URL)
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
//                "url": "htps://example.org:9091"
//            }
//        """
//
//        RestAssured.given()
//            .header("Content-Type", "application/json")
//            .body(body)
//            .basePath(Constants.ENDPOINTS_BASE_URL)
//            .put(expectedId)
//            .then()
//            .statusCode(400)
//    }
//
//    @Test
//    fun `GIVEN an existing endpoint id WHEN delete THEN return 200 `() {
//        val expectedId = createTestEndpoint()
//
//        RestAssured.given()
//            .basePath(Constants.ENDPOINTS_BASE_URL)
//            .delete(expectedId)
//            .then()
//            .statusCode(200)
//    }
//
//    @Test
//    fun `GIVEN an existing endpoint id WHEN delete another endpoint THEN return 404 `() {
//        createTestEndpoint()
//
//        RestAssured.given()
//            .basePath(Constants.ENDPOINTS_BASE_URL)
//            .delete(Id.create().get())
//            .then()
//            .statusCode(404)
//    }
//
//    private fun createTestEndpoint(): String? {
//        val endpointJson = """
//            {
//                "url": "http://acme.org/path?param=foo"
//            }
//        """
//
//        val response = RestAssured.given()
//            .contentType(ContentType.JSON)
//            .body(endpointJson)
//            .post(Constants.ENDPOINTS_BASE_URL)
//            .then()
//            .statusCode(Response.Status.CREATED.statusCode)
//            .extract()
//            .response()
//
//        return response.jsonPath().getString("id")
//    }
//
}
