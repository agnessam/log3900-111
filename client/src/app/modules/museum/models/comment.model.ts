import { User } from "../../users/models/user";

export interface CommentInterface {
  content: string;
  author: User;
  postId: string;

  createdAt?: string;
  updatedAt?: string;
}
