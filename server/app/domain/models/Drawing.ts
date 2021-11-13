import mongoose, { Document, Model, Schema } from 'mongoose';

export interface DrawingInterface extends Document {
  dataUri: string;
  ownerId: string;
  ownerModel: string;
  name: string;

  createdAt: string;
  updatedAt: string;
}

const DrawingSchema = new mongoose.Schema(
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
  },
  { timestamps: true },
);

export const Drawing: Model<DrawingInterface> = mongoose.model(
  'Drawing',
  DrawingSchema,
);
