package com.lurkerbot.discordUser

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import org.litote.kmongo.save

data class DiscordUserRepository(private val mongoClient: MongoClient) {
    private fun getUserCollection(): MongoCollection<UserInDiscord> {
        val database = mongoClient.getDatabase("lurker-bot")
        return database.getCollection<UserInDiscord>("discord_db_user")
    }

    fun getUserInDiscord(userId: String): UserInDiscord? {
        val collection = getUserCollection()

        return collection.findOne(UserInDiscord::userId eq userId)
    }

    fun saveUserInDiscord(user: UserInDiscord) {
        val collection = getUserCollection()
        collection.save(user)
    }

    fun removeUserInDiscord(user: UserInDiscord) {
        val collection = getUserCollection()
        collection.deleteOne(UserInDiscord::userId eq user.userId)
    }
}
