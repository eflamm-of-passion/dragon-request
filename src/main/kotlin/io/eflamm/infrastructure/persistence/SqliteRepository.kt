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

    override fun getEndpoint(id: Id): Result<Endpoint> {
        try {
            val query = """
                SELECT * FROM endpoints WHERE id = ?
            """.trimIndent()

            val endpoint = executeSearchQuery(query) { statement -> statement.setString(1, id.get()) }
            return if (endpoint != null) {
                Result.success(endpoint)
            } else {
                Result.failure(NoSuchElementException("No endpoint exist with the id ${id.get()}"))
            }
        } catch (e: SQLException) {
            return Result.failure(e)
        }
    }

    override fun createEndpoint(endpoint: Endpoint): Result<Endpoint> {
        try {
            val query = """
                INSERT INTO endpoints (id, protocol, domain, port, path, queryParameters) VALUES (?,?,?,?,?,?)
            """.trimIndent()

            executeChangeQuery(query) { statement ->
                statement.setString(1, endpoint.id.get())
                statement.setString(2, endpoint.protocol.get())
                statement.setString(3, endpoint.domain.get())
                statement.setInt(4, endpoint.port.get())
                statement.setString(5, endpoint.path.aggregate())
                statement.setString(6, endpoint.queryParameters.aggregate())
            }

            return getEndpoint(Id.fromString(endpoint.id.get()))
        } catch (e: SQLException) {
            return Result.failure(e)
        }
    }

    override fun updateEndpoint(endpointUpdated: Endpoint): Result<Endpoint> {
        try {
            val query = """
                UPDATE endpoints SET 
                    protocol = ?, 
                    domain = ?, 
                    port = ?, 
                    path = ?, 
                    queryParameters = ?
                WHERE id = ?
            """.trimIndent()

            executeChangeQuery(query) { statement ->
                statement.setString(1, endpointUpdated.protocol.get())
                statement.setString(2, endpointUpdated.domain.get())
                statement.setInt(3, endpointUpdated.port.get())
                statement.setString(4, endpointUpdated.path.aggregate())
                statement.setString(5, endpointUpdated.queryParameters.aggregate())
                statement.setString(6, endpointUpdated.id.get())
            }

            return getEndpoint(endpointUpdated.id)
        } catch (e: SQLException) {
            return Result.failure(e)
        }
    }

    override fun deleteEndpoint(id: Id): Result<Unit> {
        try {
            val query = """
                DELETE FROM endpoints WHERE id = ?
            """.trimIndent()

            val numberOfRowsDeleted = executeChangeQuery(query) { statement ->
                statement.setString(1, id.get())
            }

            return if(numberOfRowsDeleted > 0) {
                Result.success(Unit)
            } else {
                Result.failure(NoSuchElementException())
            }
        } catch (e: SQLException) {
            return Result.failure(e)
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

    private fun executeSearchQuery(
        query: String,
        setRequestParameters: (statement: PreparedStatement) -> Unit
    ): Endpoint? {
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

    private fun executeChangeQuery(query: String, setRequestParameters: (statement: PreparedStatement) -> Unit): Int {
        checkConnection()
        connection.prepareStatement(query).use { statement ->
            setRequestParameters.invoke(statement)
            return statement.executeUpdate() // TODO handle the result
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
        if (this::connection.isInitialized) {
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

    private fun checkConnection() {
        if (!this::connection.isInitialized) {
            connect()
        }
    }

    fun connect() {
        try {
            Class.forName("org.sqlite.JDBC")  // Explicitly load the SQLite driver
            connection = DriverManager.getConnection(Constants.SQLITE_DRIVER + databaseFilePath)
            initDatabase()
            println("Connected to database")
        } catch (e: SQLException) {
            throw e
        }
    }

}