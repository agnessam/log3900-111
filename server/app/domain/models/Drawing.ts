import mongoose, { Document, Model } from "mongoose";

export interface DrawingInterface extends Document {
  dataUri: string;
}

const DrawingSchema = new mongoose.Schema({
  dataUri: { type: Buffer, required: true }
}, { timestamps: true})

export const Drawing: Model<DrawingInterface> = mongoose.model('Drawings', DrawingSchema);