import { CommentInterface } from "./comment.model";

export interface PostInterface extends Document {
  _id: string;
  dataUri: string;
  ownerId: string;
  ownerModel: string;
  name: string;

  comments: CommentInterface[];
  likes: string[];
}
