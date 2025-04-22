package io.eflamm.dragonrequest.ui.model

sealed interface ApiFile {
    /**
     * Add an intended type of file at the end of the list of children files.
     */
    fun addFile(fileToAdd: ApiFile): Result<Int>

    /**
     * Move the index of one child file inside the list. If it has the same as another file, the other file's index get incremented, as long as the following files.
     */
    fun moveFile()

    /**
     * Removes a file from the list of children files.
     */
    fun removeFile()

    /**
     * Gets the next available index in the children file.
     */
    fun nextFileIndex(): Int
}
