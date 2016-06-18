"use strict";

class Counter {
    constructor() {
        this.counter = {};
    }

    count(word) {
        if (word in this.counter) {
            word[counter]++;
        } else {
            word[counter] = 0;
        }
    }

    get arrayify() {
        var resultArr = []

        // Each entry in the array should be of the format [word, count].
        for (var key in this.counter) {
            resultArr.push([key, this.counter[key]]);
        }

        return resultArr;
    }

    get current() {
        return this.counter;
    }
}

var counter = new Counter();

module.exports = counter;