package gameTime

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import java.time.LocalDateTime
import mu.KotlinLogging
import org.litote.kmongo.*
import response.GameTimeSum

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

    override fun getSummedTimeRecordsFor(userId: String, from: LocalDateTime): List<GameTimeSum> =
        getCollection()
            .aggregate<GameTimeSum>(
                match(TimeRecord::userId eq userId),
                group(
                    TimeRecord::gameName,
                    GameTimeSum::time sum
                        ("round".projection from
                            listOf(
                                "divide".projection from
                                    listOf(
                                        "subtract".projection from
                                            listOf(
                                                TimeRecord::sessionEnd,
                                                TimeRecord::sessionBegin
                                            ),
                                        3600000
                                    ),
                                2
                            ))
                ),
                sort(descending(GameTimeSum::time)),
                project(
                    GameTimeSum::gameName from "_id".projection,
                    GameTimeSum::time from GameTimeSum::time
                )
            )
            .toList()
}
