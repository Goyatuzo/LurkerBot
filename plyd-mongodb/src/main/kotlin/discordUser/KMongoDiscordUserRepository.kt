package discordUser

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import mu.KotlinLogging
import org.litote.kmongo.*

class KMongoDiscordUserRepository(private val mongoClient: MongoClient) : DiscordUserRepository {
    private val logger = KotlinLogging.logger {}

    private fun getUserCollection(): MongoCollection<UserInDiscord> {
        val database = mongoClient.getDatabase("lurker-bot")
        return database.getCollection<UserInDiscord>("discord_db_user")
    }

    override fun getAllUsers(): Map<String, UserInDiscord> =
        getUserCollection().find().associateBy { it.userId }

    override fun getUserInDiscord(userId: String): UserInDiscord? =
        getUserCollection().findOne(UserInDiscord::userId eq userId)

    override fun saveUserInDiscord(user: UserInDiscord) {
        val collection = getUserCollection()

        if (getUserInDiscord(user.userId) == null) {
            collection.save(user)
            logger.info { "Saved new user: ${user.username}#${user.discriminator}" }
        } else {
            collection.updateOne(
                UserInDiscord::userId eq user.userId,
                set(
                    UserInDiscord::username setTo user.username,
                    UserInDiscord::discriminator setTo user.discriminator
                )
            )
            logger.info { "Updated user: ${user.username}#${user.discriminator}" }
        }
    }

    override fun removeUserInDiscord(user: UserInDiscord) {
        val collection = getUserCollection()
        collection.deleteOne(UserInDiscord::userId eq user.userId)
        logger.info { "Deleting new user: ${user.username}#${user.discriminator}" }
    }
}
