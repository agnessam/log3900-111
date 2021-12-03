import { User } from "src/app/modules/users/models/user";

export interface Post {
  _id: string;

  owner: string | User;
  ownerModel: string;

  name: string;
  dataUri: string;

  comments: string[];
  likes: string[];

  createdAt: Date;
  updatedAt: Date;
}
