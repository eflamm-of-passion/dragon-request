package io.eflamm.infrastructure.persistence

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.*
import io.eflamm.domain.repository.EndpointRepository
import io.eflamm.infrastructure.cdi.properties.KeyValueFilePropertyProvider
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.UUID

class SqliteRepository : EndpointRepository {

    object Constants {
        const val FILE_PATH_PROPERTY = "database.sqlite.file-path"
        const val SQLITE_DRIVER = "jdbc:sqlite:"
    }

    private lateinit var connection: Connection

    fun connect() {
        try {
            connection = DriverManager.getConnection(Constants.SQLITE_DRIVER + getDatabaseFilePath())
            initDatabase()
            println("Connected to database")
        } catch (e: SQLException) {
            println("Failed to connect to database : ${e.message}")
        }
    }

    private fun getDatabaseFilePath(): String {
        val filePath = KeyValueFilePropertyProvider().get(Constants.FILE_PATH_PROPERTY)
        return javaClass.classLoader.getResource(filePath)?.path
            ?: throw IllegalStateException("Database file not found")
    }

    private fun initDatabase() {
        val query = """
            CREATE TABLE IF NOT EXISTS endpoints (
                id TEXT PRIMARY KEY,
                protocol TEXT NOT NULL,
                domain TEXT NOT NULL,
                port INTEGER NOT NULL,
                path TEXT NOT NULL,
                queryParameters TEXT NOT NULL
            )
        """.trimIndent()

        executeQuery(query)
    }

    fun disconnect() {
        if(this::connection.isInitialized) {
            connection.close()
            println("Disconnected to database")
        }

    }

    override fun getEndpoint(id: Id): Endpoint? {
        val query = """
            SELECT * FROM endpoints WHERE id = ?
        """.trimIndent()

        return executeSearchQuery(query) { statement -> statement.setString(1, id.get())}
    }

    override fun createEndpoint(endpoint: Endpoint): Endpoint {
        val query = """
            INSERT INTO endpoints (id, protocol, domain, port, path, queryParameters) VALUES (?,?,?,?,?,?)
        """.trimIndent()

        val uuidOfCreatedEndpoint = UUID.randomUUID().toString()

        executeCreateOrUpdateQuery(query) { statement ->
            statement.setString(1, uuidOfCreatedEndpoint)
            statement.setString(2, endpoint.protocol.get())
            statement.setString(3, endpoint.domain.get())
            statement.setInt(4, endpoint.port.get())
            statement.setString(5, endpoint.path.aggregate())
            statement.setString(6, endpoint.queryParameters.aggregate())
        }

        return getEndpoint(Id.fromString(uuidOfCreatedEndpoint))!!
    }

    override fun updateEndpoint(idOfEndpointToUpdate: Id, endpointUpdated: Endpoint): Endpoint {
        TODO("Not yet implemented")
    }

    override fun deleteEndpoint(id: Id) {
        TODO("Not yet implemented")
    }

    private fun createEndpointFromResultSet(resultSet: ResultSet): Endpoint {
        return Endpoint(
            Id.fromString(resultSet.getString("id")),
            Protocol.fromString(resultSet.getString("protocol")),
            DomainName(resultSet.getString("domain")),
            Port(resultSet.getInt("port")),
            Path.fromString(resultSet.getString("path")),
            QueryParameters(emptyMap())
        )
    }

    private fun executeQuery(query: String) {
        checkConnection()
        connection.createStatement().use { statement ->
            statement.executeQuery(query)
        }
    }

    private fun executeSearchQuery(query: String, setRequestParameters: (statement: PreparedStatement) -> Unit): Endpoint? {
        checkConnection()
        connection.prepareStatement(query).use { statement ->
            setRequestParameters.invoke(statement)
            val resultSet = statement.executeQuery()
            return if (resultSet.next()) {
                createEndpointFromResultSet(resultSet)
            } else {
                null
            }
        }
    }

    private fun executeCreateOrUpdateQuery(query: String, setRequestParameters: (statement: PreparedStatement) -> Unit) {
        checkConnection()
        connection.prepareStatement(query).use { statement ->
            setRequestParameters.invoke(statement)
            statement.executeUpdate() // TODO handle the result
        }
    }

    private fun checkConnection() {
        if (!this::connection.isInitialized) {
           connect()
        }
    }
}