package com.lurkerbot.gameTime

import com.lurkerbot.core.gameTime.TimeRecord

sealed class GameTimeError

data class NeverStartedLogging(val userId: String, val guildId: String) : GameTimeError()

data class StateChangedTooFast(val userId: String, val record: TimeRecord) : GameTimeError()

data class GameIsAlreadyLogging(val userId: String, val newRecord: TimeRecord) : GameTimeError()
