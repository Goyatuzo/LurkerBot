package com.lurkerbot.internal

import com.mongodb.client.MongoClient
import org.litote.kmongo.KMongo

class MongoDatabase {
    companion object {
        private val mongoClient: MongoClient = KMongo.createClient(System.getenv("LURKER_BOT_DB"))

        val client: MongoClient
            get() = mongoClient
    }
}
