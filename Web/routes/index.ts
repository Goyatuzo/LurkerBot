/*
 * GET home page.
 */
import express = require('express');

export function index(req: express.Request, res: express.Response) {
    res.render('index');
};

export function server(req: express.Request, res: express.Response) {
    res.render('server');
};