package io.eflamm.infrastructure.cdi

import jakarta.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
)
annotation class TestQualifier