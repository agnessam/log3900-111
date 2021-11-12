import mongoose, { Document, Model, Schema } from "mongoose";

export interface PublishedDrawingInterface extends Document {
  dataUri: string;
  ownerId: string;
  ownerModel: string;
  name: string;

  // TODO: Add comments and likes as a seperate model
}

const PublishedDrawingSchema = new Schema({
  dataUri: {type: String, required: true},
  ownerId: {
    type: Schema.Types.ObjectId,
    required: true,
    refPath: 'ownerModel'
  },
  ownerModel: {
    type: String,
    required: true,
    enum: ['User', 'Team']
  },
  name: {
    type: String,
    required: true
  }
}, {timestamps: true})

export const PublishedDrawing: Model<PublishedDrawingInterface> = mongoose.model('PublishedDrawing', PublishedDrawingSchema);