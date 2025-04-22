package io.eflamm.dragonrequest.ui.model

class Workspace(
    private var name: String,
) : ApiFile {
    private val children = mutableListOf<ApiFile>()

    override fun addFile(fileToAdd: ApiFile): Result<Int> =
        when (fileToAdd) {
            is Endpoint -> addChild(fileToAdd)
            is Collection -> addChild(fileToAdd)
            is Workspace -> Result.failure(IllegalArgumentException("${fileToAdd::class} cannot be added to ${this::class}"))
        }

    private fun addChild(fileToAdd: ApiFile): Result<Int> {
        children.add(fileToAdd)
        val childIndex = children.size
        return Result.success(childIndex)
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
