import mongoose, { Document, Model, Schema } from 'mongoose';

export interface TeamInterface extends Document {
  name: string;
  description: string;

  ownerId: string;
  members: string[];

  drawings: string[];
  publishedDrawings: string[];
}

const TeamSchema = new mongoose.Schema({
  name: { type: String, required: true, index: { unique: true } },
  description: { type: String },

  ownerId: { type: Schema.Types.ObjectId, required: true, ref: 'User' },
  members: [{ type: Schema.Types.ObjectId, ref: 'User' }],

  drawings: [{ type: Schema.Types.ObjectId, ref: 'Drawing' }],
  publishedDrawings: [{ type: Schema.Types.ObjectId, ref: 'PublishedDrawing'}]
});

export const Team: Model<TeamInterface> = mongoose.model('Team', TeamSchema);
