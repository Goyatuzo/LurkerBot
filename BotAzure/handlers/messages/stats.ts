import {Message, Client, TextChannel} from "discord.js";
import * as _ from "lodash";

import {getDurationSumTimeSorted} from "../../database/times_table";

function _stringifyTime(seconds: number) {
    var m = Math.floor(seconds / 60);
    var secs = seconds % 60;

    var h = Math.floor(m / 60);
    var minutes = m % 60;

    var days = Math.floor(h / 24);
    var hours = Math.floor(h % 24);
    return days + ' days ' + hours + ' hours ' + minutes + ' minutes ' + secs + ' seconds';
}

function _formatResults(results: any) {
    let summary = "";

    results.map(row => {
        summary += `${row.name}\n - ${_stringifyTime(row.duration) }\n`;
    });
    return summary;
}

export default function (message: Message) {
    const tokens = _.drop(message.cleanContent.split(" "));
    const client = message.client;

    const sortTerm = _.drop(message.cleanContent.split(" sortby "))[0];

    // If the user only requests stats, then print out the server's stats.
    if (tokens.length === 0) {
        const users = (message.channel as TextChannel).server.members;
        const userIds = users.map(user => user);
        
        getDurationSumTimeSorted(userIds, results => {
            client.sendMessage(message.channel, _formatResults(results));
        });
    }
}