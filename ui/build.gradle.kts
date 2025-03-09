plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(uilibs.plugins.composeMultiplatform) apply false
    alias(uilibs.plugins.composeCompiler) apply false
    alias(uilibs.plugins.kotlinMultiplatform) apply false
    alias(uilibs.plugins.kotlinSerialization) apply false
}