@file:Suppress("DataClassPrivateConstructor")

package com.lurkerbot.core.response

@kotlinx.serialization.Serializable
data class TimeGraphData
private constructor(val name: String, val time: Double, val colorHex: String) {
    companion object {
        fun of(name: String, time: Double): TimeGraphData {
            val hex = String.format("#%06x", (0xFFFFFF and name.hashCode()))
            return TimeGraphData(name, time, hex)
        }
    }
}
