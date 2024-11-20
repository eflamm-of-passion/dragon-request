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

    implementation("org.slf4j:slf4j-api:2.0.16")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}