package io.eflamm.dragonrequest.infrastructure.cdi.properties

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class ApplicationPropertiesFileProviderTest {

    @Test
    fun `GIVEN the provider is created WHEN get the property THEN returns the property value`() {
        // given
        val provider = ApplicationPropertiesFileProvider("application.properties")
        // we suppose that the property do exist in the gradle.properties file
        val propertyKey = "database.sqlite.file-path"
        val expectedPropertyValue = "sqlite-database.db"

        // when
        val actualPropertyValue = provider.get(propertyKey)

        // then
        assertThat(actualPropertyValue).isEqualTo(expectedPropertyValue)
    }
}