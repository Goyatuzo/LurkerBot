package gameTime

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import mu.KotlinLogging
import org.litote.kmongo.*

class KMongoTimerRepository(private val mongoClient: MongoClient) : TimerRepository {
    private val logger = KotlinLogging.logger {}

    private fun getCollection(): MongoCollection<TimeRecord> {
        val database = mongoClient.getDatabase("lurker-bot")
        return database.getCollection<TimeRecord>("game_time")
    }

    override fun saveTimeRecord(record: TimeRecord) {
        val collection = getCollection()

        collection.insertOne(record)
        logger.info { "Inserted: $record" }
    }

    override fun getSummedTimeRecordsFor(userId: String): List<TimeRecord> {
        val collection = getCollection()

        val aggregate = collection.aggregate(listOf(match(TimeRecord::userId eq userId)))

        println(aggregate.json)

        return aggregate.toList()
    }
}
