package io.eflamm.dragonrequest.repository.mongodb

import com.mongodb.client.result.InsertOneResult
import org.bson.Document

interface MongoConnector {
    fun connect()

    fun disconnect()

    fun cleanDatabase()

    fun find(): List<Document>

    fun insert(document: Document): InsertOneResult
}
