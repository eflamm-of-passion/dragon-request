package io.eflamm.dragonrequest.repository.sqlite

import io.eflamm.dragonrequest.domain.exception.EndpointException
import io.eflamm.dragonrequest.domain.exception.ErrorType
import io.eflamm.dragonrequest.domain.model.Endpoint
import io.eflamm.dragonrequest.domain.model.endpoint.*
import io.eflamm.dragonrequest.domain.monitoring.Logger
import io.eflamm.dragonrequest.domain.repository.EndpointRepository
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class SqliteRepository(private val databaseFilePath: String, private val logger: Logger) : EndpointRepository {

    object Constants {
        const val SQLITE_DRIVER = "jdbc:sqlite:"
    }

    private lateinit var connection: Connection
    override fun getEndpoints(): Result<List<Endpoint>> {
        try {
            val endpoints = executeSearchQuery("SELECT * FROM endpoints") { _ -> { } }
            return Result.success(endpoints)
        } catch (e: SQLException) {
            return Result.failure(EndpointException(ErrorType.TECHNICAL_ERROR, "Something went wrong with the database.", e))
        }
    }

    override fun getEndpoint(id: Id): Result<Endpoint> {
        try {
            val endpoints = executeSearchQuery("SELECT * FROM endpoints WHERE id = ?") { statement -> statement.setString(1, id.get()) }
            return if (endpoints.isNotEmpty()) {
                Result.success(endpoints.first())
            } else {
                Result.failure(EndpointException(ErrorType.ENTITY_NOT_FOUND, "No endpoint exists with the id ${id.get()}"))
            }
        } catch (e: SQLException) {
            return Result.failure(EndpointException(ErrorType.TECHNICAL_ERROR, "Something went wrong with the database.", e))
        }
    }

    override fun createEndpoint(endpoint: Endpoint): Result<Endpoint> {
        try {
            val query = "INSERT INTO endpoints (id, httpMethod, protocol, domain, port, path, queryParameters) VALUES (?,?,?,?,?,?,?)"

            executeChangeQuery(query) { statement ->
                statement.setString(1, endpoint.id.get())
                statement.setString(2, endpoint.httpMethod.toString())
                statement.setString(3, endpoint.protocol.get())
                statement.setString(4, endpoint.domain.get())
                statement.setInt(5, endpoint.port.get())
                statement.setString(6, endpoint.path.aggregate())
                statement.setString(7, endpoint.queryParameters.aggregate())
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
                    httpMethod = ?,
                    domain = ?, 
                    port = ?, 
                    path = ?, 
                    queryParameters = ?
                WHERE id = ?
            """.trimIndent()

            executeChangeQuery(query) { statement ->
                statement.setString(1, endpointUpdated.protocol.get())
                statement.setString(2, endpointUpdated.httpMethod.toString())
                statement.setString(3, endpointUpdated.domain.get())
                statement.setInt(4, endpointUpdated.port.get())
                statement.setString(5, endpointUpdated.path.aggregate())
                statement.setString(6, endpointUpdated.queryParameters.aggregate())
                statement.setString(7, endpointUpdated.id.get())
            }

            return getEndpoint(endpointUpdated.id)
        } catch (e: SQLException) {
            return Result.failure(e)
        }
    }

    override fun deleteEndpoint(id: Id): Result<Unit> {
        try {
            val numberOfRowsDeleted = executeChangeQuery("DELETE FROM endpoints WHERE id = ?") { statement ->
                statement.setString(1, id.get())
            }

            return if(numberOfRowsDeleted > 0) {
                Result.success(Unit)
            } else {
                Result.failure(EndpointException(ErrorType.ENTITY_NOT_FOUND, "No endpoint exists with the id ${id.get()}."))
            }
        } catch (e: SQLException) {
            return Result.failure(e)
        }
    }

    private fun createEndpointFromResultSet(resultSet: ResultSet): Endpoint {
        return Endpoint(
            Id.fromString(resultSet.getString("id")),
            HttpMethod.valueOf(resultSet.getString("httpMethod")),
            Protocol.fromString(resultSet.getString("protocol")),
            DomainName(resultSet.getString("domain")),
            Port(resultSet.getInt("port")),
            Path.fromString(resultSet.getString("path")),
            QueryParameters.fromString(resultSet.getString("queryParameters"))
        )
    }

    private fun executeSearchQuery(
        query: String,
        setRequestParameters: (statement: PreparedStatement) -> Unit
    ): List<Endpoint> {
        checkConnection()
        connection.prepareStatement(query).use { statement ->
            setRequestParameters.invoke(statement)
            val resultSet = statement.executeQuery()
            val endpoints = mutableListOf<Endpoint>()
            while (resultSet.next()) {
                endpoints.add(createEndpointFromResultSet(resultSet))
            }
            return endpoints
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
                httpMethod TEXT NOT NULL,
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
            logger.info("Disconnected to database")
        }

    }

    fun clearDatabase() {
        connection.createStatement().use { statement ->
            statement.execute("DROP TABLE IF EXISTS endpoints")
        }
    }

    fun cleanDatabase() {
        connection.createStatement().use { statement ->
            statement.execute("DELETE FROM endpoints")
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
            logger.info("Connected to database")
        } catch (e: SQLException) {
            throw e
        }
    }

}
