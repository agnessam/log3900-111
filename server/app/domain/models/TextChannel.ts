import mongoose, { Document, Model } from 'mongoose';
import { Message } from '../interfaces/message.interface';

export interface TextChannelInterface extends Document{
    channelName: string;
    owner: string,
    messages: Message[];
}

const TextChannelSchema = new mongoose.Schema({
    // does the channel name have to be unique?
	channelName: { type: String, required: true, index: { unique: true } },
	description: String,

	owner: { type: String, required: true, index: { unique: true } },
    // TODO: look for custom types in mongoose
	messages: { type: Message[], required: true },
});

export const TextChannel: Model<TextChannelInterface> = mongoose.model('TextChannels', TextChannelSchema);