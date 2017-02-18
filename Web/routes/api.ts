/*
 * GET users listing.
 */
import express = require('express');

import * as Times from '../api/times';

export function getSummaryByUser(req: express.Request, res: express.Response) {
    Times.getTimesByUser(result => {
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