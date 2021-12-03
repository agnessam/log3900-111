import mongoose, { Document, Model } from 'mongoose';
export interface TextChannelInterface extends Document {
  name: string;
  ownerId: string;
  team: string;
  drawing: string;

  isPrivate: boolean;
}

const TextChannelSchema = new mongoose.Schema({
  name: { type: String, required: true },
  ownerId: { type: String, required: true },
  team: { type: String },
  drawing: { type: String },

  isPrivate: { type: Boolean, default: false },
});

export const TextChannel: Model<TextChannelInterface> = mongoose.model(
  'TextChannels',
  TextChannelSchema,
);
