package com.lurkerbot.core.error

import com.lurkerbot.core.gameTime.TimeRecord

object UserNotFound : DomainError()

data class NeverStartedPlaying(val userId: String) : DomainError()

data class LoggingInDifferentServer(val userId: String, val guildId: String) : DomainError()

data class StateChangedTooFast(val userId: String, val record: TimeRecord) : DomainError()

data class GameIsAlreadyLogging(val userId: String, val newRecord: TimeRecord) : DomainError()
