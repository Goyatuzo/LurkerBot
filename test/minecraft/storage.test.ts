import { expect } from 'chai';
import mcData from '../../bot/tools/minecraft/storage';

describe("Minecraft data storage", () => {
    describe("death counter", () => {
        describe("shot by a [entity]", () => {
            it("skeleton", () => {
                mcData.processLine("2017-02-17 22:20:21 [INFO] tjaing3521 was shot by Skeleton");
                expect(mcData.getDeathsFor("tjaing3521").entries.length).to.equal(1);
                expect(mcData.getDeathsFor("tjaing3521").entries[0].text).to.eql("was shot by Skeleton");
            });
        });
    });
});