import UserSettings from "./userSettings.model";
export default class Users {
  userId!: number;
  userName!: string;
  biography!: string;
  picture!: string;
  settings!:UserSettings

}

export class UsersLogIn {
  userName!: string;
  email!: string;
  password!: string;
  biography!: string;
}

export interface userBasicInfo {
  userName: string;
  picture: string;
}
