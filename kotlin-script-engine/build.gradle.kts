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
    implementation(project(":shared-model"))

    implementation(libs.kotlinScriptingJvm)
    implementation(libs.kotlinScriptingJvmHost)

    testImplementation(libs.kotlinTest)
    testImplementation(project(":stub"))

    testImplementation(libs.mockk)
    testImplementation(libs.kotestAssertions)
    testImplementation(libs.junitJupiter)

    testImplementation(libs.mockitoCore)
    testImplementation(libs.mockitoKotlin)
    testImplementation(libs.mockitoInline)
}

tasks.test {
    useJUnitPlatform()
}
