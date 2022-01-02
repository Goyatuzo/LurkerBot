package gameTime

import org.litote.kmongo.*
import com.mongodb.client.MongoClient

data class TimerRepository(
    private val mongoClient: MongoClient
) {
    fun saveTimeRecord(record: TimeRecord) {
        val database = mongoClient.getDatabase("lurker-bot")
        val collection = database.getCollection<TimeRecord>("game_time")

        collection.insertOne(record)
    }
}