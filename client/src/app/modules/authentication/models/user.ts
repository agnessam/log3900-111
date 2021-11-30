import { Avatar } from "src/app/shared/models/avatar.model";
import { PrivacySetting } from "src/app/shared/models/privacy-setting.interface";

export class User {
  _id: string;
  username: string;
  avatar: Avatar;
  password: string;
  firstName: string;
  lastName: string;
  email: string;
  description: string;

  privacySetting: PrivacySetting;

  public constructor(init?: Partial<User>) {
    Object.assign(this, init);
  }
}
