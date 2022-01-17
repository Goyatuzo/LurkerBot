package com.lurkerbot

import dev.kord.common.annotation.KordPreview
import dev.kord.core.event.user.PresenceUpdateEvent
import dev.kord.gateway.Intents
import me.jakejmattson.discordkt.api.dsl.bot
import me.jakejmattson.discordkt.api.dsl.precondition
import me.jakejmattson.discordkt.api.extensions.intentsOf

@OptIn(KordPreview::class)
fun main() {
    bot(System.getenv("LURKER_BOT_TOKEN")) {
        precondition { if (author.isBot) fail("Bots cannot invoke anything") }

        configure {
            intents = Intents.nonPrivileged + intentsOf<PresenceUpdateEvent>()
            generateCommandDocs = false
        }
    }
}
