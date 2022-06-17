package response

@kotlinx.serialization.Serializable
data class GameTimeSum(val _id: String, val gameName: String, val time: Double)
