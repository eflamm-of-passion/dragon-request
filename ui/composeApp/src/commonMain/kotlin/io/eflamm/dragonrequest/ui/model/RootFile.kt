package io.eflamm.dragonrequest.ui.model

object RootFile : ApiFile {
    val children: MutableList<InternalApiFile> = mutableListOf()

    override fun addFile(fileToAdd: InternalApiFile): Result<Int> =
        when (fileToAdd) {
            is Workspace -> addWorkspace(fileToAdd)
            else -> Result.failure(IllegalArgumentException("${fileToAdd::class} cannot be added to ${this::class}"))
        }

    private fun addWorkspace(workspaceToAdd: Workspace): Result<Int> {
        this.children.add(workspaceToAdd)
        workspaceToAdd.parent = this
        return Result.success(this.children.size)
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
