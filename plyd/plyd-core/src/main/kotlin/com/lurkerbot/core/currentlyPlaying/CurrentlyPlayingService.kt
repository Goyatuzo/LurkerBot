package com.lurkerbot.core.currentlyPlaying

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.toResultOr
import com.lurkerbot.core.error.DomainError
import com.lurkerbot.core.error.UserNotPlaying

class CurrentlyPlayingService(private val currentlyPlayingRepository: CurrentlyPlayingRepository) {
    fun save(currentlyPlaying: CurrentlyPlaying) {
        currentlyPlayingRepository.save(currentlyPlaying)
    }
    fun getByUserId(userId: String): Result<CurrentlyPlaying, DomainError> =
        currentlyPlayingRepository.getByDiscordUserId(userId).toResultOr { UserNotPlaying }

    fun removeByUserId(userId: String) {
        currentlyPlayingRepository.removeByDiscordUserId(userId)
    }
    fun isUserCurrentlyPlayingById(userId: String): Boolean =
        currentlyPlayingRepository.getByDiscordUserId(userId) != null
    fun clearAll() = currentlyPlayingRepository.clearCurrentlyPlaying()
}
