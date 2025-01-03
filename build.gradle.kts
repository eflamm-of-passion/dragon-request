plugins {
    kotlin("jvm") version "2.0.20"
    id("io.quarkus")
}

group = "io.eflamm.dragonrequest"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation(project(":repository-sqlite"))
    implementation(project(":logger-slf4j"))
    testImplementation(project(":repository-sqlite"))

    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-kotlin")

    implementation("ch.qos.logback:logback-classic:1.5.12")
    implementation("com.google.code.gson:gson:2.11.0")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")

    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.26.3")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
