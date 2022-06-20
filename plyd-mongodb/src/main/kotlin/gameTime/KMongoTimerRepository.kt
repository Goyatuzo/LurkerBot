package gameTime

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import java.time.LocalDateTime
import mu.KotlinLogging
import org.litote.kmongo.*
import response.GameTimeDetailedSum
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
                match(TimeRecord::userId eq userId, TimeRecord::sessionEnd gte from),
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

    override fun getSummedGameTimeRecordsFor(
        userId: String,
        gameName: String,
        from: LocalDateTime
    ): List<GameTimeDetailedSum> =
        getCollection()
            .aggregate<GameTimeDetailedSum>(
                match(
                    TimeRecord::userId eq userId,
                    TimeRecord::gameName eq gameName,
                    TimeRecord::sessionEnd gte from
                ),
                group(
                    fields(
                        TimeRecord::gameDetail from TimeRecord::gameDetail,
                        TimeRecord::gameState from TimeRecord::gameState,
                        TimeRecord::smallAssetText from TimeRecord::smallAssetText,
                        TimeRecord::largeAssetText from TimeRecord::largeAssetText
                    ),
                    GameTimeDetailedSum::time sum
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
                            )),
                    GameTimeDetailedSum::detail first TimeRecord::gameDetail
                ),
                sort(descending(GameTimeSum::time)),
                project(
                    mapOf(
                        GameTimeDetailedSum::detail to "_id.gameDetail".projection,
                        GameTimeDetailedSum::state to "_id.gameState".projection,
                        GameTimeDetailedSum::smallAsset to "_id.smallAssetText".projection,
                        GameTimeDetailedSum::detail to "_id.gameDetail".projection,
                        GameTimeDetailedSum::largeAsset to "_id.largeAssetText".projection,
                        GameTimeDetailedSum::time to GameTimeDetailedSum::time
                    )
                )
            )
            .toList()
}
