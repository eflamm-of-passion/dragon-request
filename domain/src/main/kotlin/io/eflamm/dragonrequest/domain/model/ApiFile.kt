package io.eflamm.dragonrequest.domain.model

import io.eflamm.dragonrequest.domain.model.common.Id

sealed interface ApiFile {
    fun getFiles(): List<ApiFile>

    /**
     * Add an intended type of file at the end of the list of children files.
     */
    fun addFile(fileToAdd: InternalApiFile): Result<Int>

    /**
     * Remove all the files contained in this file
     */
    fun removeAllFiles()

    val id: Id
    val name: ApiFilename
}

abstract class InternalApiFile(
    override val id: Id,
    override val name: ApiFilename,
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

@JvmInline
value class ApiFilename(
    val value: String,
) {
    override fun toString(): String = value
}
