/*
 * GET users listing.
 */
import express = require('express');

import {getTimesByUser, getTimesFromServer} from '../api/times';

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