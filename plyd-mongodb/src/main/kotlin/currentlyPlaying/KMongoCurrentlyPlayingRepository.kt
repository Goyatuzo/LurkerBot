package currentlyPlaying

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import mu.KotlinLogging
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import org.litote.kmongo.save

class KMongoCurrentlyPlayingRepository(private val mongoClient: MongoClient) :
    CurrentlyPlayingRepository {
    private val logger = KotlinLogging.logger {}

    private fun getCurrentlyPlayingCollection(): MongoCollection<CurrentlyPlaying> {
        val database = mongoClient.getDatabase("lurker-bot")
        return database.getCollection<CurrentlyPlaying>("currently_playing")
    }
    override fun save(currentlyPlaying: CurrentlyPlaying) {
        val collection = getCurrentlyPlayingCollection()

        collection.save(currentlyPlaying)
        logger.info { "Saved currently playing: $currentlyPlaying" }
    }

    override fun getByDiscordUserId(userId: String): CurrentlyPlaying? {
        val collection = getCurrentlyPlayingCollection()
        return collection.findOne(CurrentlyPlaying::userId eq userId)
    }

    override fun removeByDiscordUserId(userId: String) {
        val collection = getCurrentlyPlayingCollection()
        collection.deleteMany(CurrentlyPlaying::userId eq userId)
        logger.info { "Deleting currently playing for user: $userId" }
    }

    override fun clearCurrentlyPlaying() {
        getCurrentlyPlayingCollection().drop()
    }
}
