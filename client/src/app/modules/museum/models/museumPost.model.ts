import { Comment } from "./comment.model";

export interface MuseumPost {
  image: string;
  timestamp: string;
  author: string;
  like: number;
  comments: Comment[];
}
