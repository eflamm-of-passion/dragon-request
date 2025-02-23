plugins {
    kotlin("jvm") version "2.0.20"
}

group = "io.eflamm.dragonrequest"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":infrastructure"))
}

tasks.register("package") {
    group = "custom"
    description = ""
    dependsOn(":infrastructure:jar")
}
tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
