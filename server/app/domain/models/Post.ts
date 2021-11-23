import mongoose, { Document, Model, Schema } from 'mongoose';
import { CommentInterface } from './Comment';

export interface PostInterface extends Document {
  dataUri: string;
  ownerId: string;
  ownerModel: string;
  name: string;

  comments: string[] | CommentInterface[];
  likes: string[];
}

const PostSchema = new Schema(
  {
    dataUri: { type: String, required: true },
    ownerId: {
      type: Schema.Types.ObjectId,
      required: true,
      refPath: 'ownerModel',
    },
    ownerModel: {
      type: String,
      required: true,
      enum: ['User', 'Team'],
    },
    name: {
      type: String,
      required: true,
    },
    comments: [{ type: Schema.Types.ObjectId, ref: 'Comment' }],
    likes: [{ type: Schema.Types.ObjectId, ref: 'User' }],
  },
  { timestamps: true },
);

export const Post: Model<PostInterface> = mongoose.model(
  'PublishedDrawing',
  PostSchema,
);
