plugins {
    kotlin("jvm")
}

group = "io.eflamm.dragonrequest"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.slf4j)
    implementation(libs.logback)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}