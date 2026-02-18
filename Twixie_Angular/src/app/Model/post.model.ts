import Users from './users.model';

export default class Post {
  postId!: number;
  poster!: Users;
  topicId!: number;
  topicName!: string;
  content!: string;
  title!: string;
  datePosted!: Date;
  lastUpdated!: Date;
  imagePath!: string;
}
