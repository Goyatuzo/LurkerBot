package com.lurkerbot.gameTime

import com.mongodb.client.MongoClient
import mu.KotlinLogging
import org.litote.kmongo.*

data class TimerRepository(private val mongoClient: MongoClient) {
    private val logger = KotlinLogging.logger {}

    fun saveTimeRecord(record: TimeRecord) {
        val database = mongoClient.getDatabase("lurker-bot")
        val collection = database.getCollection<TimeRecord>("game_time")

        logger.info { "Attempting to save $record" }
        collection.insertOne(record)
        logger.info { "Inserted: $record" }
    }
}
