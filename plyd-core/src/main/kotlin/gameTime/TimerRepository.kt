package gameTime

interface TimerRepository {
    fun saveTimeRecord(record: TimeRecord)
    fun getSummedTimeRecordsFor(userId: String): List<TimeRecord>
}
