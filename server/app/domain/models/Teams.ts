import mongoose, { Document, Model, Schema } from 'mongoose';

export interface TeamInterface extends Document {
  name: string;
  description: string;

  owner: string;
  members: string[];

  drawings: string[];
}

const TeamSchema = new mongoose.Schema({
  name: { type: String, required: true, index: { unique: true } },
  description: { type: String },

  owner: { type: Schema.Types.ObjectId, required: true, ref: 'User' },
  members: [{ type: Schema.Types.ObjectId, ref: 'User' }],

  drawings: [{ type: Schema.Types.ObjectId, ref: 'Drawing' }],
});

export const Team: Model<TeamInterface> = mongoose.model('Team', TeamSchema);
