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
    implementation("ch.qos.logback:logback-classic:1.5.25")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}