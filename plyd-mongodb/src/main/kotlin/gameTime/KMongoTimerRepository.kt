package gameTime

import com.mongodb.client.MongoClient
import mu.KotlinLogging
import org.litote.kmongo.getCollection

class KMongoTimerRepository(private val mongoClient: MongoClient) : TimerRepository {
    private val logger = KotlinLogging.logger {}

    override fun saveTimeRecord(record: TimeRecord) {
        val database = mongoClient.getDatabase("lurker-bot")
        val collection = database.getCollection<TimeRecord>("game_time")

        collection.insertOne(record)
        logger.info { "Inserted: $record" }
    }
}
