package io.eflamm.dragonrequest.repository.mongodb

import io.eflamm.dragonrequest.domain.exception.DragonRequestAppException
import io.eflamm.dragonrequest.domain.exception.ErrorType
import io.eflamm.dragonrequest.domain.model.ApiFile
import io.eflamm.dragonrequest.domain.model.ApiFilename
import io.eflamm.dragonrequest.domain.model.Collection
import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.model.InternalApiFile
import io.eflamm.dragonrequest.domain.model.Workspace
import io.eflamm.dragonrequest.domain.model.common.Id
import io.eflamm.dragonrequest.domain.model.endpoint.DomainName
import io.eflamm.dragonrequest.domain.model.endpoint.HttpMethod
import io.eflamm.dragonrequest.domain.model.endpoint.Path
import io.eflamm.dragonrequest.domain.model.endpoint.Port
import io.eflamm.dragonrequest.domain.model.endpoint.Protocol
import io.eflamm.dragonrequest.domain.model.endpoint.QueryParameters
import io.eflamm.dragonrequest.domain.monitoring.Logger
import io.eflamm.dragonrequest.domain.repository.ApiFilesRepository
import io.eflamm.dragonrequest.domain.repository.CollectionRepository
import io.eflamm.dragonrequest.domain.repository.EndpointRepository
import io.eflamm.dragonrequest.domain.repository.WorkspaceRepository
import org.bson.Document

class MongoRepository(
    private val mongoConnector: MongoConnector,
    private val logger: Logger,
) : ApiFilesRepository,
    WorkspaceRepository,
    CollectionRepository,
    EndpointRepository {
    override fun getAllApiFiles(): Result<List<ApiFile>> {
        val documents = mongoConnector.find()
        val mapOfApiFilesById: Map<String, ApiFile> = documents.map { it.toApiFile() }.associateBy { it.id.toString() }
        val listOfApiFilesByParentId =
            documents
                .filter(this::isNotWorkspace)
                .map { document ->
                    val parentId = document.getString("parentId") // TODO all this is ugly
                    val apiFile =
                        mapOfApiFilesById[document.getString("id")]!! // FIXME is there a better way to get in the map
                    parentId to apiFile
                }.filter { mapOfApiFilesById.keys.contains(it.component2().id.toString()) } // TODO make a function, remove the orphans

        // TODO make a function, assign all the children to their parents
        listOfApiFilesByParentId.forEach {
            val parentId = it.component1()
            if (parentId != null) {
                val parent = mapOfApiFilesById.getValue(parentId) // TODO handle when the parentId does not exist
                val child = it.component2() as InternalApiFile
                parent.addFile(child)
            }
        }

        // TODO make a function, get all roots
        val workspaces = mapOfApiFilesById.values.filter { it is Workspace }

        return Result.success(workspaces)
    }

    private fun isNotWorkspace(document: Document) = document.getString("type") != "workspace"

    override fun create(workspace: Workspace): Result<Workspace> =
        try {
            val documentToInsert = workspace.toDocument()
            mongoConnector.insert(documentToInsert) // TODO check the result coming from Mongo
            Result.success(workspace)
        } catch (e: Exception) {
            logger.error("Something happened when creating a workspace in database", e)
            Result.failure(
                DragonRequestAppException(ErrorType.TECHNICAL_ERROR, "Something happened when creating a workspace in database", e),
            )
        }

    override fun create(
        collection: Collection,
        parentId: Id,
    ): Result<Collection> =
        try {
            val documentToInsert = collection.toDocument(parentId)
            mongoConnector.insert(documentToInsert) // TODO check the result coming from Mongo
            Result.success(collection)
        } catch (e: Exception) {
            logger.error("Something happened when creating a collection in database", e)
            Result.failure(
                DragonRequestAppException(ErrorType.TECHNICAL_ERROR, "Something happened when creating a collection in database", e),
            )
        }

    override fun create(
        endpoint: Endpoint,
        parentId: Id,
    ): Result<Endpoint> =
        try {
            val documentToInsert = endpoint.toDocument(parentId)
            mongoConnector.insert(documentToInsert) // TODO check the result coming from Mongo
            Result.success(endpoint)
        } catch (e: Exception) {
            logger.error("Something happened when creating a endpoint in database", e)
            Result.failure(
                DragonRequestAppException(ErrorType.TECHNICAL_ERROR, "Something happened when creating a endpoint in database", e),
            )
        }

    // TODO handle the workspace
    // TODO handle the collection
    // TODO handle the endpoint

    fun connect() = mongoConnector.connect()

    fun disconnect() = mongoConnector.disconnect()

    fun cleanDatabase() = mongoConnector.cleanDatabase()

    private fun Workspace.toDocument(): Document =
        Document()
            .append("type", "workspace")
            .append("id", id.toString())
            .append("name", name.toString())

    private fun Collection.toDocument(parentId: Id): Document =
        Document()
            .append("type", "collection")
            .append("parentId", parentId.toString())
            .append("id", id.toString())
            .append("name", name.toString())

    private fun Endpoint.toDocument(parentId: Id): Document =
        Document()
            .append("type", "endpoint")
            .append("parentId", parentId.toString())
            .append("id", id.toString())
            .append("name", name.toString())
            .append("httpMethod", httpMethod.toString())
            .append("protocol", protocol.get())
            .append("domain", domain.toString())
            .append("path", path.toString())

    fun Document.toApiFile(): ApiFile =
        when (getString("type")?.lowercase()) {
            "workspace" -> toWorkspace()
            "collection" -> toCollection()
            "endpoint" -> toEndpoint()
            else -> throw IllegalArgumentException("Unknown type: ${getString("type")}")
        }

    fun Document.toWorkspace(): Workspace =
        Workspace(
            id = Id.fromString(getString("id")),
            name = ApiFilename(getString("name")),
        )

    fun Document.toCollection(): Collection =
        Collection(
            id = Id.fromString(getString("id")),
            name = ApiFilename(getString("name")),
        )

    fun Document.toEndpoint(): Endpoint =
        Endpoint(
            id = Id.fromString(getString("id")),
            name = ApiFilename(getString("name")),
            httpMethod = HttpMethod.valueOf(getString("httpMethod")),
            protocol = Protocol.fromString(getString("protocol")),
            domain = DomainName(getString("domain")),
            port = Port(80), // parse if present
            path = Path(listOf(getString("path"))),
            queryParameters = QueryParameters(emptyMap()),
        )
}
