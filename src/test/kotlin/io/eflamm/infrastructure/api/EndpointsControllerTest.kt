package io.eflamm.infrastructure.api
import io.eflamm.infrastructure.cdi.TestQualifier
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import jakarta.inject.Inject
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import java.util.UUID

@QuarkusTest
class EndpointsControllerTest {

    @Inject
    @TestQualifier
    lateinit var endpointsController: EndpointsController

    @Test
    fun getEndpointTest() {
        val expectedId = UUID.fromString("a48cd2ec-3b61-4908-b3ec-add927cd9e09").toString()

        RestAssured.given()
            .`when`()["/endpoints/" + expectedId]
            .then()
            .statusCode(200)
            .body("id", CoreMatchers.equalTo(expectedId))
            .body("protocol", CoreMatchers.equalTo("http"))
            .body("domain", CoreMatchers.equalTo("acme.org"))
            .body("port", CoreMatchers.equalTo("80"))
            .body("path", CoreMatchers.equalTo(""))
            .body("queryParameters", CoreMatchers.equalTo(""))
    }
}