package com.lurkerbot.discordUser

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

data class DiscordUserRepository(private val mongoClient: MongoClient) {
    private fun getUserCollection(): MongoCollection<UserInDiscord> {
        val database = mongoClient.getDatabase("lurker-bot")
        return database.getCollection<UserInDiscord>("discord_db_user")
    }

    fun getAllUsers(): Map<String, UserInDiscord> =
        getUserCollection().find().associateBy { it.userId }

    fun getUserInDiscord(userId: String): UserInDiscord? {
        return getUserCollection().findOne(UserInDiscord::userId eq userId)
    }
}
