package com.lurkerbot.discordUser

import com.lurkerbot.gameTime.TimeRecord
import com.mongodb.client.MongoClient
import org.litote.kmongo.getCollection

data class DiscordUserRepository(
    private val mongoClient: MongoClient
) {
    fun saveUserInDiscord(user: UserInDiscord) {
        val database = mongoClient.getDatabase("lurker-bot")
        val collection = database.getCollection<UserInDiscord>("discord_db_user")

        collection.insertOne(user)
        println("Inserted: $user")
    }
}