import Users from './users.model';

export default class Response {
  responseId!: number;
  responser!: Users;
  parentPostResponseId!: number;
  parentResponseID!: number;
  content!: string;
  dateCreated!: Date;
  deleted!: boolean;
  children: Response[] = [];
}
