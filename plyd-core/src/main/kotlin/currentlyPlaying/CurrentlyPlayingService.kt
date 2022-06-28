package currentlyPlaying

class CurrentlyPlayingService(private val currentlyPlayingRepository: CurrentlyPlayingRepository) {
    fun save(currentlyPlaying: CurrentlyPlaying) =
        currentlyPlayingRepository.saveCurrentlyPlaying(currentlyPlaying)
    fun getByUserId(userId: String): CurrentlyPlaying =
        currentlyPlayingRepository.getCurrentlyPlayingByDiscordUserId(userId)
    fun removeByUserId(userId: String): CurrentlyPlaying =
        currentlyPlayingRepository.removeCurrentlyPlayingByDiscordUserId(userId)
    fun clearAll() = currentlyPlayingRepository.clearCurrentlyPlaying()
}
