pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "dragon-request"
include("domain")
include("application")
include("repository-mongodb")
include("logger-slf4j")
include("infrastructure")
// include("ui") // FIXME gradle depencencies between root and submodules
