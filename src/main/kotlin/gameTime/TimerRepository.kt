package gameTime

import com.mongodb.client.MongoClient

data class TimerRepository(
    private val mongoClient: MongoClient
) {
    fun saveTimeRecord(record: TimeRecord) {

    }
}