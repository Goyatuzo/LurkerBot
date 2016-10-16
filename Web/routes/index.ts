/*
 * GET home page.
 */
import express = require('express');
import * as Times from '../api/times';

export function index(req: express.Request, res: express.Response) {
    res.render('index');
};

export function query(req: express.Request, res: express.Response) {

    const userId = req.query['userId'];
    const serverId = req.query['serverId'];
    
    if (userId) {
        Times.getTimesFromUser(userId.toString(), result => {
            result = JSON.parse(JSON.stringify(result));
            res.render('query', { data: result });
        });
    }

    if (serverId) {
        Times.getTimesFromServer(serverId.toString(), result => {
            res.render('query', { data: result });
        });
    }
};