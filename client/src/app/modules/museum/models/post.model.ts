import { CommentInterface } from "./comment.model";
import { User } from "../../users/models/user";
import { Team } from "src/app/shared/models/team.model";

export interface PostInterface extends Document {
  _id: string;
  dataUri: string;
  owner: User | Team;
  ownerModel: string;
  name: string;

  comments: CommentInterface[];
  likes: string[];

  createdAt?: string;
  updatedAt?: string;
}
