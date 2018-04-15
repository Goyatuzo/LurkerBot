import Timer from "../bot/tools/stats/game_timer";
import {expect} from "chai";

describe("Initialize timer,", () => {
    let timer;

    beforeEach(() => {
        timer = new Timer();
    });

    it("there should be a starting time in milliseconds.", () => {
        expect(timer.start).to.be.greaterThan(0);
    });

    it("after 1001 milliseconds, timeElapsed() should be 1.", function (done) {
        setInterval(() => {
            expect(timer.timeElapsed()).to.equal(1);
            done();
        }, 1001);
    });
});