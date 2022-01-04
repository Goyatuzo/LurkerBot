package com.lurkerbot.gameTime

import org.litote.kmongo.*
import com.mongodb.client.MongoClient
import mu.KotlinLogging

data class TimerRepository(
    private val mongoClient: MongoClient
) {
    private val logger = KotlinLogging.logger {}

    fun saveTimeRecord(record: TimeRecord) {
        val database = mongoClient.getDatabase("lurker-bot")
        val collection = database.getCollection<TimeRecord>("game_time")

        collection.insertOne(record)
        logger.info { "Inserted: $record" }
    }
}