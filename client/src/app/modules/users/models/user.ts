import { Avatar } from "src/app/shared/models/avatar.model";

export interface User {
  _id?: string;
  username?: string;
  avatar?: Avatar;
  password?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  description?: string;
}
