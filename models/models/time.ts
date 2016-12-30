import { Table, Column, ManyToMany, JoinTable, ManyToOne, JoinColumn, PrimaryGeneratedColumn } from 'typeorm';
import * as uuid from 'uuid';

import User from './user';

@Table()
export default class Time {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    endTime: Date;

    @Column()
    duration: number;

    @Column()
    gameName: string;

    @ManyToOne(type => User, user => user.times, {
        cascadeAll: true
    })
    user: User;
}