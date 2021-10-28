import mongoose, { Document, Model, Schema } from 'mongoose';
import { Message } from '../interfaces/message.interface';

export interface TextChannelInterface extends Document{
    name: string;
    ownerId: string;
    messages: Message[];
}

const TextChannelSchema = new mongoose.Schema({
	name: { type: String, required: true, index: { unique: true } },
	ownerId: { type: String, required: true },
    // Type translates to 'any'
	messages: { type: Schema.Types.Mixed },
});

export const TextChannel: Model<TextChannelInterface> = mongoose.model('TextChannels', TextChannelSchema);