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

    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")
    implementation("com.google.code.gson:gson:2.11.0") // TODO replace gson by jackson

    implementation(platform("io.vertx:vertx-stack-depchain:4.5.11"))
    implementation("io.vertx:vertx-web:4.5.11")
    testImplementation(kotlin("test"))
    
    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation("io.rest-assured:rest-assured:5.5.0")
    testImplementation("org.hamcrest:hamcrest:3.0")
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