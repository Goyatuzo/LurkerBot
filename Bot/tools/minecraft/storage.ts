const causesOfDeath = [
    // Arrows
    /was show by arrow/,
    /was shot by \w*/,
    /was shot by \w* using \w*/,
    // Cactus
    /was pricked to death/,
    /walked into a cactus while trying to escape \w*/,
    // Drowning
    /drowned/,
    /drowned whilst trying to escape \w*/,
    // Eleytra
    /experienced kinetic energy/,
    // Explosion,
    /blew up/,
    /was blown up by \w*/,
    // Falling
    /hit the ground too hard/,
    /fell from a high place/,
    /fell from a ladder/,
    /fell off some vines/,
    /fell out of the water/,
    /fell into a patch of fire/,
    /fell into a patch of cacti/,
    /was doomed to fall by \w*/,
    /was shot off some vines by \w*/,
    /was shot off a ladder by \w*/,
    /was blown from a high place by \w*/,
    // Falling blocks,
    /was squashed by a falling \w*/,
    // Fire
    /went up in flames/,
    /burned to death/,
    /was burnt to a crisp whilst fighting \w*/,
    /walked into a fire whilst fighting \w*/,
    // Lava
    /tried to swim in lava/,
    /tried to swim in lava while trying to escape \w*/,
    // Lightning,
    /was struck by lightning/,
    // Magma Block,
    /discovered floor was lava/,
    // Players and mobs
    /was slain by \w*/,
    /was slain \w* using \*/,
    /got finished off by \w*/,
    /got finished off by \w* using \w*/,
    /was fireballed by \w*/,
    // Potions of harming
    /was killed by magic/,
    /was killed by \w* using magic/,
    // Starvation
    /starved to death/,
    // Suffocation
    /suffocated in a wall/,
    /was squished too much/,
    // Thorns enchantment
    /was killed while trying to hurt \w*/,
    // Void
    /fell out of the world/,
    /fell from a high place and fell out of the world/,
    // Wither
    /withered away/,
    // Other
    /was pummeled by \w*/,
    /died/
];

export interface DeathDTO {
    playerName: string;
    causes: Array<string>;
}

class McDataStore {
    private deaths: { [player: string]: Array<string> };

    constructor() {
        this.deaths = {};
    }

    private separateTimeFromMessage(line: string): Array<string> {
        // Fix this, because if someone types [INFO], everything will break.
        return line.split(/\s*\[INFO\] | \[SEVERE\] | \[WARNING\]\s*/g);
    }

    private addDeath(player: string, cause: string): void {
        if (player in this.deaths) {
            this.deaths[player].push(cause);
        } else {
            this.deaths[player] = [cause];
        }
    }

    private processDeath(line: string) {
        const player = line.match(/^\w*/);
        const cause = line.replace(/^\w*\s/, '');

        causesOfDeath.forEach(causeRegEx => {
            if (cause.match(causeRegEx)) {
                this.addDeath(player[0], cause);
            }
        });
    }

    public processLine(line: string) {
        const tokens = this.separateTimeFromMessage(line);

        // Message string.
        if (tokens[1].match(/^<\w*>/)) {

        } else if (tokens[1].match(/^\w*/)) {
            this.processDeath(tokens[1]);
        } else {
            console.error(`Cannot process: ${tokens[1]}`);
        }
    }

    public getDeathsFor(player: string): Array<string> {
        return this.deaths[player];
    }

    get deathsArray(): Array<DeathDTO> {
        return Object.keys(this.deaths).map(player => {
            return {
                playerName: player,
                causes: this.deaths[player]
            };
        });
    }

    get playerList(): Array<string> {
        return Object.keys(this.deaths);
    }
}

let mcData = new McDataStore();
export default mcData;