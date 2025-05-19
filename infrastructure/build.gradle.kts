plugins {
    kotlin("jvm")
    application
}

group = "io.eflamm.dragonrequest"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation(project(":repository-sqlite"))
    implementation(project(":logger-slf4j"))

    implementation(libs.jackson.module)
    implementation(libs.jackson.databind)
    implementation(libs.gson) // TODO replace gson by jackson

    implementation(platform(libs.vertx.stack))
    implementation(libs.vertx.web)
    testImplementation(kotlin("test"))

    testImplementation(libs.assertj)
    testImplementation(libs.restassured)
    testImplementation(libs.hamcrest)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("io.eflamm.dragonrequest.infrastructure.cdi.ApplicationDependencyInjectorKt")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "io.eflamm.dragonrequest.infrastructure.cdi.ApplicationDependencyInjectorKt"
    }
    from(
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) },
    )
}
