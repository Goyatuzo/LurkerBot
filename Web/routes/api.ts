/*
 * GET users listing.
 */
import express = require('express');
import {getTimesByUser} from '../api/times';

export function getSummaryByUser(req: express.Request, res: express.Response) {
    getTimesByUser(result => {
        res.json(JSON.stringify(result));
    });
};
