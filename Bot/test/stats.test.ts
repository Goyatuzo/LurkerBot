import {expect} from "chai";
import {User} from "discord.js";

import {Stats} from "../tools/stats/stats";

describe("Initialize stats,", () => {
    let stats: Stats;

    beforeEach(() => {
        stats = new Stats();
    });

    it("the stats object should be completely empty.", () => {
        expect(stats.stats).to.be.empty;
    });

    it("getting a time that doesn't exist should return null.", () => {
        const playerA = {
            id: "abc",
            name: "PlayerA"
        } as User;

        expect(stats.timePlayed(playerA, "Starcraft")).to.be.null;
    });

    it("no game should exists, so exists function should return false.", () => {
        const playerA = {
            id: "abc",
            name: "PlayerA"
        } as User;

        expect(stats.exists(playerA, "Starcraft")).to.be.false;
    });

    describe("PlayerA is playing Starcraft,", () => {
        let playerA: User;

        beforeEach(() => {
            playerA = {
                id: "abc",
                name: "PlayerA"
            } as User;

            stats.addGame(playerA, "Starcraft");
        });

        it("gameExists for PlayerA and Starcraft should be true.", () => {
            expect(stats.exists(playerA, "Starcraft")).to.be.true;
        });

        it("timePlayed should return a number.", () => {
            expect(typeof (stats.timePlayed(playerA, "Starcraft"))).to.equal("number");
        });

        describe("and then PlayerA stops playing Starcraft.", () => {
            beforeEach(() => {
                stats.removeGame(playerA, "Starcraft");
            });

            it("the stats object should consist of only playerA's id.", () => {
                const expected = { 'abc': {} };

                expect(stats.stats).to.deep.equal(expected);
            });
        });
    });
});