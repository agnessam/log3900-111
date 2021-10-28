import mongoose, { Document, Model } from 'mongoose';
export interface TextChannelInterface extends Document{
    name: string;
    ownerId: string;
}

const TextChannelSchema = new mongoose.Schema({
	name: { type: String, required: true, index: { unique: true } },
	ownerId: { type: String, required: true },
});

export const TextChannel: Model<TextChannelInterface> = mongoose.model('TextChannels', TextChannelSchema);