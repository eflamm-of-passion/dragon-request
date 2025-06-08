package io.eflamm.dragonrequest.ui.model

import io.eflamm.dragonrequest.ui.model.states.ApiFileState

sealed interface ApiFile {
    fun getFiles(): List<ApiFile>

    /**
     * Add an intended type of file at the end of the list of children files.
     */
    fun addFile(fileToAdd: InternalApiFile): Result<Int>

    /**
     * Move the index of one child file inside the list. If it has the same as another file, the other file's index get incremented, as long as the following files.
     */
    fun moveFile()

    /**
     * Removes a file from the list of children files.
     */
    fun removeFile()

    /**
     * Remove all the files contained in this file
     */
    fun removeAllFiles()

    /**
     * Gets the next available index in the children file.
     */
    fun nextFileIndex(): Int

    val name: String
}

abstract class InternalApiFile(
    val id: String,
    override val name: String,
    protected var state: ApiFileState,
) : ApiFile {
    var parent: ApiFile? = null
    private val children: MutableList<InternalApiFile> = mutableListOf()

    override fun addFile(fileToAdd: InternalApiFile): Result<Int> {
        this.children.add(fileToAdd)
        fileToAdd.parent = this
        return Result.success(this.children.size)
    }

    override fun removeAllFiles() = children.clear()

    override fun getFiles(): List<ApiFile> = children.toList()
}
