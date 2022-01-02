package gameTime

sealed class GameTimeError

data class NeverStartedLogging(val userId: String, val gameName: String) : GameTimeError()
data class GameIsAlreadyLogging(
    val userId: String,
    val gameName: String,
    val record: TimeRecord
) : GameTimeError()