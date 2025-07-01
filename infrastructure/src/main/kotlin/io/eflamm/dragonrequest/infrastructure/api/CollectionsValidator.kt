package io.eflamm.dragonrequest.infrastructure.api

import java.util.stream.Collectors

// TODO should unit test
abstract class CollectionsValidator {
    companion object Methods {
        fun validate(input: CollectionCreateInput): String =
            listOf(isNameBlank(input.name))
                .stream()
                .collect(Collectors.joining())

        fun validate(
            id: String,
            input: CollectionUpdateInput,
        ): String =
            listOf(areIdsSame(id, input.id))
                .stream()
                .collect(Collectors.joining())

        fun doesNotContainErrors(errorMessage: String): Boolean = errorMessage.isEmpty()

        private fun areIdsSame(
            idFromPath: String,
            idFromBody: String,
        ): String =
            if (idFromPath == idFromBody) {
                ""
            } else {
                "The ids from path and body do not match: $idFromPath and $idFromBody. "
            }

        private fun isNameBlank(name: String): String =
            if (name.isBlank()) {
                "Name cannot be blank. "
            } else {
                ""
            }
    }
}
