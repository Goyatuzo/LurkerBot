package com.lurkerbot.internal

import com.mongodb.client.MongoClient
import org.litote.kmongo.KMongo

object MongoDatabase {
    val client: MongoClient = KMongo.createClient(System.getenv("LURKER_BOT_DB"))
}
