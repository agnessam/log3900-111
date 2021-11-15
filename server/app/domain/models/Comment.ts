import mongoose, { Document, Model, Schema } from 'mongoose';

export interface CommentInterface extends Document {
  content: string;
  authorId: string;
  publishedDrawingId: string;

  createdAt: string;
  updatedAt: string;
}

const CommentSchema = new mongoose.Schema(
  {
    content: { type: String, required: true },
    authorId: {
      type: Schema.Types.ObjectId,
      required: true,
      ref: 'User',
    },
    publishedDrawingId: {
      type: Schema.Types.ObjectId,
      required: true,
      ref: 'PublishedDrawing',
    },
  },
  { timestamps: true },
);

export const Comment: Model<CommentInterface> = mongoose.model(
  'Comment',
  CommentSchema,
);
