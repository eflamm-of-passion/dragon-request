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
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
