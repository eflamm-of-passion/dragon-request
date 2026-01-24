plugins {
    alias(libs.plugins.kotlinJvm)
}

group = "io.eflamm.dragon-request"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.slf4j)
    implementation(libs.logback)

    testImplementation(libs.kotlinTest)
}

tasks.test {
    useJUnitPlatform()
}
