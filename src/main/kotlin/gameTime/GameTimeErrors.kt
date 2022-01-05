package com.lurkerbot.gameTime

sealed class GameTimeError

data class NeverStartedLogging(val userId: String, val guildId: String) : GameTimeError()

data class GameIsAlreadyLogging(
    val userId: String,
    val alreadyLogging: TimeRecord,
    val newRecord: TimeRecord
) : GameTimeError()
