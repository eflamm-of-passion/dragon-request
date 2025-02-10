plugins {
    kotlin("jvm")
}

group = "io.eflamm.dragonrequest"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.sqlite)

    testImplementation(kotlin("test"))
    testImplementation(libs.assertj)
    testImplementation(libs.mockk)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}