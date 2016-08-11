/*
 * GET users listing.
 */
import express = require('express');

import {getTimesByUser, getTimesFromServer, getTimesFromUser} from '../api/times';

export function getSummaryByUser(req: express.Request, res: express.Response) {
    getTimesByUser(result => {
        res.json(JSON.stringify(result));
    });
};

export function getSummaryFromServer(req: express.Request, res: express.Response) {
    const serverId = req.query['serverId'];

    getTimesFromServer(serverId.toString(), result => {
        res.json(JSON.stringify(result));
    });
}

export function getSummaryFromUser(req: express.Request, res: express.Response) {
    const userId = req.query['userId'];

    getTimesFromUser(userId.toString(), result => {
        res.json(JSON.stringify(result));
    });
}