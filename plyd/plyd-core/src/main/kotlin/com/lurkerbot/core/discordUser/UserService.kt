package com.lurkerbot.core.discordUser

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.toResultOr
import com.lurkerbot.core.error.DomainError
import com.lurkerbot.core.error.UserNotFound

class UserService(private val discordUserRepository: DiscordUserRepository) {
    fun getUserByDiscordId(userId: String): Result<UserInDiscord, DomainError> =
        discordUserRepository.getUserInDiscord(userId).toResultOr { UserNotFound }
}
