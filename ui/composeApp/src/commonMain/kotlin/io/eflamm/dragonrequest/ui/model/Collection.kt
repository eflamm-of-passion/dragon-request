package io.eflamm.dragonrequest.ui.model

class Collection(
    private var name: String,
) : InternalApiFile() {
    override fun addFile(fileToAdd: InternalApiFile): Result<Int> =
        when (fileToAdd) {
            is Endpoint -> super.addFile(fileToAdd)
            is Collection -> super.addFile(fileToAdd)
            else -> Result.failure(IllegalArgumentException("${fileToAdd::class} cannot be added to ${this::class}"))
        }

    override fun moveFile() {
        TODO("Not yet implemented")
    }

    override fun removeFile() {
        TODO("Not yet implemented")
    }

    override fun nextFileIndex(): Int {
        TODO("Not yet implemented")
    }
}
