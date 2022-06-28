package currentlyPlaying

import com.mongodb.client.MongoClient

class KMongoCurrentlyPlayingRepository(mongoClient: MongoClient) : CurrentlyPlayingRepository {
    override fun saveCurrentlyPlaying(currentlyPlaying: CurrentlyPlaying) {
        TODO("Not yet implemented")
    }

    override fun getCurrentlyPlayingByDiscordUserId(userId: String): CurrentlyPlaying {
        TODO("Not yet implemented")
    }

    override fun removeCurrentlyPlayingByDiscordUserId(userId: String): CurrentlyPlaying {
        TODO("Not yet implemented")
    }

    override fun clearCurrentlyPlaying() {
        TODO("Not yet implemented")
    }
}
