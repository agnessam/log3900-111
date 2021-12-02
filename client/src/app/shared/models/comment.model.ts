import { User } from "src/app/modules/users/models/user";

export interface Comment {
  owner: User;

  postId: string;
  content: string;

  createdAt: string;
  updatedAt: string;
}
