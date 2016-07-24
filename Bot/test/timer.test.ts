import Timer from "../tools/timer";
import {expect} from "chai";

describe("Initialize timer,", () => {
    let timer;

    beforeEach(() => {
        timer = new Timer();
    });

    it("the time elapsed should be greater than 0.", done => {
        setInterval(() => {
            console.log(timer.timeElapsed());
            done();
        }, 2);
    });
});