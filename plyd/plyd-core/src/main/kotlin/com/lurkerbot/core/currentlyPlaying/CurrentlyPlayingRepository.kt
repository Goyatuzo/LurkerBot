package com.lurkerbot.core.currentlyPlaying

interface CurrentlyPlayingRepository {
    fun save(currentlyPlaying: CurrentlyPlaying)

    fun getByDiscordUserId(userId: String): CurrentlyPlaying?

    fun removeByDiscordUserId(userId: String)

    fun clearCurrentlyPlaying()
}
