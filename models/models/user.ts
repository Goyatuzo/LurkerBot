import { Table, Column, PrimaryColumn, ManyToMany, JoinTable, OneToMany, JoinColumn } from 'typeorm';

import Server from './server';
import Time from './time';

@Table()
export default class User {
    @PrimaryColumn()
    id: number;

    @Column()
    name: string;

    @Column()
    avatar: string;

    @ManyToMany(type => Server, server => server.users, {
        cascadeAll: true
    })
    servers: Array<Server> = [];

    @OneToMany(type => Time, time => time.user, {
        cascadeAll: true
    })
    times: Array<Time>;
}