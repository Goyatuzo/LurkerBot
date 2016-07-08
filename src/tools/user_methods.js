"use strict";

/**
 * From a user object, extract the name and the discriminator and attach to each other with a #.
 */
function getUsername(user) {
    var name = user.username;
    var discrim = user.discriminator;

    return name + '#' + discrim.toString();
};


function getId(user) {
    return user.id;
}

/**
 * Get the name of the game being played by the user. If there is no game being played, it will
 * return undefined.
 */
function getGameName(user) {
    var game = user.game;

    // If there is a game object, then return the name.
    if (game && !user.bot) {
        return game.name;
    }

    return undefined;
};

module.exports = {
    getUniqueName: getUsername,
    getId: getId,
    getGame: getGameName
};