plugins {
    kotlin("jvm") version "2.3.0"
}

group = "io.eflamm.dragon-request"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))

    implementation("org.slf4j:slf4j-api:2.0.17")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.17") // or logback later

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}