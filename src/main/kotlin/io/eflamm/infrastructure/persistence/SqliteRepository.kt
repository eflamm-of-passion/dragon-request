package io.eflamm.infrastructure.persistence

import io.eflamm.domain.model.Endpoint
import io.eflamm.domain.model.endpoint.*
import io.eflamm.domain.repository.EndpointRepository
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.UUID

class SqliteRepository(private val databaseFilePath: String) : EndpointRepository {

    object Constants {
        const val SQLITE_DRIVER = "jdbc:sqlite:"
    }

    private lateinit var connection: Connection

    fun connect() {
        try {
            connection = DriverManager.getConnection(Constants.SQLITE_DRIVER + databaseFilePath)
            initDatabase()
            println("Connected to database")
        } catch (e: SQLException) {
            println("Failed to connect to database : ${e.message}")
        }
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
        connection.createStatement().use { statement ->
            statement.execute(query)
        }
    }

    fun disconnect() {
        if(this::connection.isInitialized) {
            connection.close()
            println("Disconnected to database")
        }

    }

    fun clearDatabase() {
        val query = """
            DROP TABLE IF EXISTS endpoints
        """.trimIndent()
        connection.createStatement().use { statement ->
            statement.execute(query)
        }
    }

    fun cleanDatabase() {
        val query = """
            DELETE FROM endpoints
        """.trimIndent()
        connection.createStatement().use { statement ->
            statement.execute(query)
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

        // FIXME should be in the domain probably
        val uuidOfCreatedEndpoint = UUID.randomUUID().toString()

        executeChangeQuery(query) { statement ->
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
        val query = """
            DELETE FROM endpoints WHERE id = ?
        """.trimIndent()

        executeChangeQuery(query) { statement ->
            statement.setString(1, id.get())
        }
    }

    private fun createEndpointFromResultSet(resultSet: ResultSet): Endpoint {
        return Endpoint(
            Id.fromString(resultSet.getString("id")),
            Protocol.fromString(resultSet.getString("protocol")),
            DomainName(resultSet.getString("domain")),
            Port(resultSet.getInt("port")),
            Path.create(), // FIXME
            QueryParameters.create() // FIXME
        )
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

    private fun executeChangeQuery(query: String, setRequestParameters: (statement: PreparedStatement) -> Unit) {
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