/*
 * GET home page.
 */
import express = require('express');

export function index(req: express.Request, res: express.Response) {
    res.render('index');
};

export function query(req: express.Request, res: express.Response) {
    res.render('query');
};