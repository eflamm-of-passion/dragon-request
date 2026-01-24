plugins {
    alias(libs.plugins.kotlinJvm)
}

group = "io.eflamm.dragon-request"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.kotlinTest)
}

tasks.test {
    useJUnitPlatform()
}
