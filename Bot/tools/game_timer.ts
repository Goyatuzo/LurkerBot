export default class GameTimer {
    start: number;

    constructor() {
        this.start = +new Date();
    }

    /**
     * Return the time elapsed.
     */
    timeElapsed() {
        const curr = +new Date();
        
        return Math.floor((curr - this.start) / 1000);
    }
}
