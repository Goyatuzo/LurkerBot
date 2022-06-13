package gameTime

interface TimerRepository {
    fun saveTimeRecord(record: TimeRecord)
}
