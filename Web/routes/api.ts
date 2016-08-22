/*
 * GET users listing.
 */
import express = require('express');

import * as Times from '../api/times';
import ITime from '../classes/i-times';

var summaryCache: Array<ITime>;

export function getSummaryByUser(req: express.Request, res: express.Response) {
    if (summaryCache) {
        res.json(JSON.stringify(summaryCache));
    }

    Times.getTimesByUser(result => {
        summaryCache = result;
        res.json(JSON.stringify(result));
    });
};

export function getSummaryFromServer(req: express.Request, res: express.Response) {
    const serverId = req.query['serverId'];

    Times.getTimesFromServer(serverId.toString(), result => {
        res.json(JSON.stringify(result));
    });
}

export function getSummaryFromUser(req: express.Request, res: express.Response) {
    const userId = req.query['userId'];

    Times.getTimesFromUser(userId.toString(), result => {
        res.json(JSON.stringify(result));
    });
}

export function getServerList(req: express.Request, res: express.Response) {
    Times.getServerList(result => {
        res.json(JSON.stringify(result));
    });
}