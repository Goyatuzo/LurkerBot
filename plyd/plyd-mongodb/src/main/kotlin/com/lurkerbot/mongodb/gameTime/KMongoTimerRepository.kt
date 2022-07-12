package com.lurkerbot.mongodb.gameTime

import com.lurkerbot.core.gameTime.TimeRecord
import com.lurkerbot.core.gameTime.TimerRepository
import com.lurkerbot.core.response.GameTimeDetailedSum
import com.lurkerbot.core.response.GameTimeSum
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import java.time.LocalDateTime
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

    override fun getSummedTimeRecordsFor(userId: String, from: LocalDateTime): List<GameTimeSum> =
        getCollection()
            .aggregate<GameTimeSum>(
                match(TimeRecord::userId eq userId, TimeRecord::sessionEnd gte from),
                group(
                    TimeRecord::gameName,
                    GameTimeSum::time sum
                        ("divide".projection from
                            listOf(
                                "subtract".projection from
                                    listOf(TimeRecord::sessionEnd, TimeRecord::sessionBegin),
                                3600000
                            )),
                    GameTimeSum::date first (TimeRecord::sessionEnd)
                ),
                sort(descending(GameTimeSum::time)),
                project(
                    GameTimeSum::gameName from "_id".projection,
                    GameTimeSum::date from GameTimeSum::date,
                    GameTimeSum::time from GameTimeSum::time
                )
            )
            .toList()

    override fun getGroupedTimeRecordsByDate(from: LocalDateTime): List<GameTimeSum> {
        val dateFormat = "%Y-%m-%d"
        val queries =
            arrayListOf(
                match(TimeRecord::sessionEnd gte from),
                group(
                    fields(
                        TimeRecord::gameName from TimeRecord::gameName,
                        TimeRecord::sessionEnd from
                            (MongoOperator.dateToString from
                                (combine(
                                    "format" from dateFormat,
                                    "date" from TimeRecord::sessionEnd
                                ))),
                    ),
                    GameTimeSum::time sum
                        ("divide".projection from
                            listOf(
                                "subtract".projection from
                                    listOf(TimeRecord::sessionEnd, TimeRecord::sessionBegin),
                                3600000
                            )),
                ),
                sort(descending(GameTimeSum::time)),
                project(
                    GameTimeSum::gameName from "_id.gameName".projection,
                    GameTimeSum::date from
                        ("dateFromString".projection from
                            (combine(
                                "format" from dateFormat,
                                "dateString" from "_id.sessionEnd".projection
                            ))),
                    GameTimeSum::time from GameTimeSum::time,
                    excludeId()
                )
            )

        return getCollection().aggregate(queries, GameTimeSum::class.java).toList()
    }

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
                        ("divide".projection from
                            listOf(
                                "subtract".projection from
                                    listOf(TimeRecord::sessionEnd, TimeRecord::sessionBegin),
                                3600000
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

    override fun mostRecentEntries(userId: String, count: Int): List<TimeRecord> =
        getCollection()
            .aggregate<TimeRecord>(
                match(TimeRecord::userId eq userId),
                sort(descending(TimeRecord::sessionEnd)),
                limit(count)
            )
            .toList()
}
