package com.lurkerbot.gameTime

import dev.kord.core.entity.Activity

fun Activity?.sameActivityAs(other: Activity?): Boolean =
    this != null &&
        other != null &&
        name == other.name &&
        details == other.details &&
        assets?.smallText == other.assets?.smallText &&
        assets?.largeText == other.assets?.largeText
