package com.lurkerbot.core.currentlyPlaying

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.lurkerbot.core.error.DomainError
import com.lurkerbot.core.error.UserNotFound

class CurrentlyPlayingService(private val currentlyPlayingRepository: CurrentlyPlayingRepository) {
    fun save(currentlyPlaying: CurrentlyPlaying) {
        currentlyPlayingRepository.save(currentlyPlaying)
    }
    fun getByUserId(userId: String): Result<CurrentlyPlaying, DomainError> {
        currentlyPlayingRepository.getByDiscordUserId(userId)?.let {
            return Ok(it)
        }

        return Err(UserNotFound)
    }
    fun removeByUserId(userId: String) {
        currentlyPlayingRepository.removeByDiscordUserId(userId)
    }
    fun isUserCurrentlyPlayingById(userId: String): Boolean =
        currentlyPlayingRepository.getByDiscordUserId(userId) != null
    fun clearAll() = currentlyPlayingRepository.clearCurrentlyPlaying()
}
