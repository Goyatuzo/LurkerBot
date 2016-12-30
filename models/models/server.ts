import { Table, Column, PrimaryColumn, ManyToMany, JoinTable } from 'typeorm';

import User from './user';

@Table()
export default class Server {
    @PrimaryColumn()
    id: number;

    @Column()
    name: string;

    @Column()
    region: string;

    @Column()
    icon: string;

    @ManyToMany(type => User, user => user.servers, {
        cascadeInsert: true,
        cascadeUpdate: true,
        cascadeRemove: true
    })
    @JoinTable()
    users: Array<User> = [];
}