export default class Timer {
    start: Date;

    constructor() {
        this.start = new Date();
    }

    /**
     * Return the time elapsed.
     */
    timeElapsed() {
        const curr: Date = new Date();

        return Math.floor((curr.getMilliseconds() - this.start.getMilliseconds()) / 1000);
    }
}
