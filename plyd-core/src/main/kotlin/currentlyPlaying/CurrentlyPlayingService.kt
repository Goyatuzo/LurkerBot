package currentlyPlaying

class CurrentlyPlayingService(private val currentlyPlayingRepository: CurrentlyPlayingRepository) {
    fun save(currentlyPlaying: CurrentlyPlaying) = currentlyPlayingRepository.save(currentlyPlaying)
    fun getByUserId(userId: String): CurrentlyPlaying? =
        currentlyPlayingRepository.getByDiscordUserId(userId)
    fun removeByUserId(userId: String) = currentlyPlayingRepository.removeByDiscordUserId(userId)
    fun clearAll() = currentlyPlayingRepository.clearCurrentlyPlaying()
}
