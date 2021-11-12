import mongoose, { Document, Model, Schema } from 'mongoose';

export interface AvatarInterface extends Document {
  imageUrl: string;
}

export const AvatarSchema = new Schema({
  imageUrl: { type: String, required: true },
});

export const Avatar: Model<AvatarInterface> = mongoose.model(
  'Avatar',
  AvatarSchema,
);
