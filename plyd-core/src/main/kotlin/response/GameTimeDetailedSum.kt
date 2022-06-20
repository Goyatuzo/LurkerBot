package response

@kotlinx.serialization.Serializable
data class GameTimeDetailedSum(
    val time: Double,
    val state: String?,
    val detail: String?,
    val smallAsset: String?,
    val largeAsset: String?
)
