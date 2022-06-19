package discordUser

class UserService(private val discordUserRepository: DiscordUserRepository) {
    fun getUserByDiscordId(userId: String): UserInDiscord? =
        discordUserRepository.getUserInDiscord(userId)
}
