import mongoose, { Document, Model, Schema } from 'mongoose';
import { UserInterface } from './user';

export interface TeamInterface extends Document {
  name: string;
  description: string;

  owner: string;
  members: string[] | UserInterface[];
  memberLimit: number;

  drawings: string[];
  posts: string[];
}

const TeamSchema = new mongoose.Schema({
  name: { type: String, required: true, index: { unique: true } },
  description: { type: String },

  owner: { type: Schema.Types.ObjectId, required: true, ref: 'User' },
  members: [{ type: Schema.Types.ObjectId, ref: 'User' }],
  memberLimit: { type: Number, min: 1 },

  drawings: [{ type: Schema.Types.ObjectId, ref: 'Drawing' }],
  posts: [{ type: Schema.Types.ObjectId, ref: 'Post' }],
});

export const Team: Model<TeamInterface> = mongoose.model('Team', TeamSchema);
