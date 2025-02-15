package io.eflamm.dragonrequest.infrastructure.cdi.properties

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class ApplicationPropertiesFileProviderTest {

    @Test
    fun `GIVEN the provider is created WHEN get the property THEN returns the property value`() {
        // given
        val provider = ApplicationPropertiesFileProvider("application-testing.properties","application.properties")
        // we suppose that the property do exist in the application-testing.properties file
        val sqlitePropertyKey = "database.sqlite.file-path"
        val expectedSqlitePropertyValue = "sqlite-database-testing.db"
        // we suppose that the property do exist in the application.properties file
        val httpPortPropertyKey = "http-server.port"
        val expectedHttpPortPropertyValue = "8080"

        // when
        val actualSqlitePropertyValue = provider.get(sqlitePropertyKey)
        val actualHttpPortPropertyValue = provider.get(httpPortPropertyKey)

        // then
        assertThat(actualSqlitePropertyValue).isEqualTo(expectedSqlitePropertyValue)
        assertThat(actualHttpPortPropertyValue).isEqualTo(expectedHttpPortPropertyValue)
    }
}