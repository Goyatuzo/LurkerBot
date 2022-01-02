package gameTime

class GameTimer(private val timerRepository: TimerRepository) {
    private val beingTracked: MutableMap<String, TimeRecord> = mutableMapOf()

    private fun generateKey(userId: String, gameName: String) = userId + gameName

    fun beginLogging(userId: String, gameName: String, record: TimeRecord) {
        beingTracked[generateKey(userId, gameName)] = record
    }

    fun endLogging(userId: String, gameName: String): TimeRecord? {
        val key = generateKey(userId, gameName)
        beingTracked[key]?.let {
            timerRepository.saveTimeRecord(it)
            return it
        }

        return null
    }
}