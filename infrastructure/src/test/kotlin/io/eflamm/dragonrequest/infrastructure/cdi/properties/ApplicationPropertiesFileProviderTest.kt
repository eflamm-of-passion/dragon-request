package io.eflamm.dragonrequest.infrastructure.cdi.properties

import io.eflamm.dragonrequest.domain.monitoring.Logger
import io.eflamm.dragonrequest.logger.slf4j.SLF4JLogger
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows

class ApplicationPropertiesFileProviderTest {

    private fun instantiateLogger(): Logger {
        return SLF4JLogger(SLF4JLogger::class.java.simpleName)
    }

    @Test
    fun `GIVEN the provider is created with correct properties WHEN validate and get properties THEN returns the properties value and no error message`() {
        // given
        val provider = ApplicationPropertiesFileProvider("application-unit-testing-success.properties","application.properties", instantiateLogger())
        // we suppose that the property do exist in the application-integration-testing.properties file
        val expectedSqlitePropertyValue = "sqlite-database-testing.db" // override
        // we suppose that the property do exist in the application.properties file
        val expectedHttpPortPropertyValue = 8080 // fallback

        // when
        val actualSqlitePropertyValue = provider.sqliteFilePath()
        val actualHttpPortPropertyValue = provider.httpServerPort()
        // TODO check the other properties

        // then
        assertThat(actualSqlitePropertyValue).isEqualTo(expectedSqlitePropertyValue)
        assertThat(actualHttpPortPropertyValue).isEqualTo(expectedHttpPortPropertyValue)
    }

    @Test
    fun `GIVEN the provider is created with both files missing WHEN validate and get properties THEN throws RuntimeException`() {
        assertThrows<RuntimeException> {
            ApplicationPropertiesFileProvider("application-not-existing.properties","application-not-existing.properties", instantiateLogger())
        }
    }

    @Test
    fun `GIVEN the provider is created with a wrong typed property WHEN validate and get properties THEN returns the properties value and no error message`() {
        assertThrows<RuntimeException> {
            ApplicationPropertiesFileProvider("application-unit-testing-wrong-type.properties","application.properties", instantiateLogger())
        }
    }

    @Test
    fun `GIVEN the provider is created with an empty property WHEN validate and get properties THEN returns the properties value and no error message`() {
        assertThrows<RuntimeException> {
            ApplicationPropertiesFileProvider("application-unit-testing-empty-string.properties","application.properties", instantiateLogger())
        }
    }

    @Test
    fun `GIVEN the provider is created with a missing property WHEN validate and get properties THEN returns the properties value and no error message`() {
        assertThrows<RuntimeException> {
            ApplicationPropertiesFileProvider("application-unit-testing-missing-properties.properties","application-unit-testing-missing-properties.properties", instantiateLogger())
        }
    }
}