/**
 * For the purposes of the API, GameTime represents the table where the entries
 * where the sum of each game times for each user is accumulated.
 */
interface ITime {
    name: string;
    gameName: string;
    duration: number;
}

export default ITime;