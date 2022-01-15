package com.lurkerbot.gameTime

import dev.kord.core.entity.Activity

fun Activity.sameActivityAs(other: Activity): Boolean =
    name == other.name &&
        details == other.details &&
        assets?.smallText == other.assets?.smallText &&
        assets?.largeText == other.assets?.largeText
