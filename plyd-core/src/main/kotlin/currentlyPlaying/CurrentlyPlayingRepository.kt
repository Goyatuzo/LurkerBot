package currentlyPlaying

interface CurrentlyPlayingRepository {
    fun saveCurrentlyPlaying(currentlyPlaying: CurrentlyPlaying)
    fun getCurrentlyPlayingByDiscordUserId(userId: String): CurrentlyPlaying
}
