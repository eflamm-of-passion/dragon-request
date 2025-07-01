package io.eflamm.dragonrequest.repository.mongodb

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.result.InsertOneResult
import io.eflamm.dragonrequest.domain.monitoring.Logger
import org.bson.Document

class MongoConnectorImpl(
    private val databaseAddress: String,
    private val mongoDatabaseName: String,
    private val mongoCollectionName: String,
    private val logger: Logger,
) : MongoConnector {
    private lateinit var client: MongoClient
    private lateinit var database: MongoDatabase
    private lateinit var collection: MongoCollection<Document>

    object Constants {
        const val MONGO_DRIVER = "mongodb://"
    }

    init {
        connect()
    }

    override fun connect() {
        // TODO add a retry system
        try {
            client = MongoClients.create(Constants.MONGO_DRIVER + databaseAddress)
            database = client.getDatabase(mongoDatabaseName)
            collection = database.getCollection(mongoCollectionName)
            logger.info("Connected to database")
        } catch (e: Exception) {
            logger.error("Failed to connect to MongoDB: ${e.message}")
            throw e
        }
    }

    override fun disconnect() {
        client.close()
        logger.info("Disconnected to database")
    }

    override fun cleanDatabase() {
        collection.deleteMany(Document())
    }

    override fun find(): List<Document> = collection.find().toList()

    override fun insert(document: Document): InsertOneResult = collection.insertOne(document)
}
