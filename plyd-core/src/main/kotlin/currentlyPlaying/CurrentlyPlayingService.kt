package currentlyPlaying

class CurrentlyPlayingService(private val currentlyPlayingRepository: CurrentlyPlayingRepository) {
    fun saveCurrentlyPlaying(currentlyPlaying: CurrentlyPlaying) = currentlyPlayingRepository.saveCurrentlyPlaying(currentlyPlaying)
    fun getCurrentlyPlayingByUserId(userId: String): CurrentlyPlaying = currentlyPlayingRepository.getCurrentlyPlayingByDiscordUserId(userId)
}