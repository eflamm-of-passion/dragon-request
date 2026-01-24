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
    implementation(project(":http-request-engine"))
    implementation(project(":kotlin-script-engine"))
    implementation(project(":slf4j-logger"))
}
