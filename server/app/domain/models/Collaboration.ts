import mongoose, { Document, Schema } from 'mongoose';

export interface CollaborationInterface extends Document {
  drawing: string;
  timeSpent: number;
}

export const CollaborationSchema = new mongoose.Schema({
  drawing: {
    type: Schema.Types.ObjectId,
    required: true,
    ref: 'Drawing',
  },
  timeSpent: {
    type: Number,
  },
});
