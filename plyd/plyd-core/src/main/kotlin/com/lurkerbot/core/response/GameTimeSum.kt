package com.lurkerbot.core.response

import kotlinx.datetime.LocalDateTime

@kotlinx.serialization.Serializable
data class GameTimeSum(val gameName: String, val date: LocalDateTime, val time: Double)
