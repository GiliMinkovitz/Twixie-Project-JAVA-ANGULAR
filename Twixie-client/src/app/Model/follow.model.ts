import Users from './users.model';

export default class Follow {
    followId!: number;
    follower!: Users;
    followee!: Users;
    followStart!: Date;
}
