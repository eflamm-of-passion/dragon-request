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
    implementation(project(":shared-model"))

    testImplementation(kotlin("test"))
    testImplementation(project(":stub"))
    testImplementation("io.mockk:mockk:1.14.7")
    testImplementation("io.kotest:kotest-assertions-core:6.0.7")
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.2")

    testImplementation("org.mockito:mockito-core:5.21.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:6.2.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")
}

tasks.test {
    useJUnitPlatform()
}
