import * as moment from 'moment';
import causesOfDeath from './causes-of-death';


export interface DeathDTO {
    playerName: string;
    entries: Array<ILogLine>;
}

export interface ILogLine {
    time: moment.Moment,
    type: string,
    text: string
}

class McDataStore {
    private deaths: { [player: string]: Array<ILogLine> };

    constructor() {
        this.deaths = {};
    }

    private getLineComponents(line: string): ILogLine {
        const split = line.split(/\s*\[INFO\] | \[SEVERE\] | \[WARNING\]\s*/g);
        const type = line.match(/\s*\[INFO\] | \[SEVERE\] | \[WARNING\]\s*/g)[0].toString();

        const time = moment(split[0]);
        const text = split[1].toString();

        // Fix this, because if someone types [INFO], everything will break.
        return {
            time: time,
            type: type,
            text: text
        };
    }

    private addDeath(player: string, lineData: ILogLine): void {
        if (player in this.deaths) {
            this.deaths[player].push(lineData);
        } else {
            this.deaths[player] = [lineData];
        }
    }

    private processDeath(line: ILogLine) {
        const player = line.text.match(/^\w*/);
        const cause = line.text.replace(/^\w*\s/, '');

        causesOfDeath.forEach(causeRegEx => {
            if (cause.match(causeRegEx)) {
                this.addDeath(player[0], line);
            }
        });
    }

    public processLine(line: string) {
        const components = this.getLineComponents(line);

        // Message string.
        if (components.text.match(/^<\w*>/)) {

        } else if (components.text.match(/^\w*/)) {
            this.processDeath(components);
        } else {
            console.error(`Cannot process: ${components.text}`);
        }
    }

    public getDeathsFor(player: string): DeathDTO {
        return {
            playerName: player,
            entries: this.deaths[player]
        };
    }

    get deathsArray(): Array<DeathDTO> {
        return Object.keys(this.deaths).map(player => {
            return {
                playerName: player,
                entries: this.deaths[player]
            };
        });
    }

    get playerList(): Array<string> {
        return Object.keys(this.deaths);
    }

    clearCache(): void {
        this.deaths = {};
    }
}

let mcData = new McDataStore();
export default mcData;