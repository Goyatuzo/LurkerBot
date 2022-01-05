package com.lurkerbot.discordUser

import com.mongodb.client.MongoClient
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

data class DiscordUserRepository(private val mongoClient: MongoClient) {
    fun getUserInDiscord(userId: String): UserInDiscord? {
        val database = mongoClient.getDatabase("lurker-bot")
        val collection = database.getCollection<UserInDiscord>("discord_db_user")

        return collection.findOne(UserInDiscord::userId eq userId)
    }
}
