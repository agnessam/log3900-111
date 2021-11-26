import mongoose, { Document, Model } from 'mongoose';
export interface TextChannelInterface extends Document{
    name: string;
    owner: string;
}

const TextChannelSchema = new mongoose.Schema({
	name: { type: String, required: true, index: { unique: true } },
	owner: { type: String, required: true },
});

export const TextChannel: Model<TextChannelInterface> = mongoose.model('TextChannels', TextChannelSchema);

export const CollaborationChannel = TextChannel.discriminator('CollaborationChannels', new mongoose.Schema({
    drawing: { type: String, required: true },
}))

export const TeamChannel = TextChannel.discriminator('TeamChannels', new mongoose.Schema({
    team: { type: String, required: true },
}))