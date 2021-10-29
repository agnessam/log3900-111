import mongoose, { Document, Model, Schema } from 'mongoose';
import { Message } from '../interfaces/message.interface';

export interface TextChannelInterface extends Document{
    channelName: string;
    ownerId: string;
    messages: Message[];
}

// ajouter schema de message ici

const TextChannelSchema = new mongoose.Schema({
	channelName: { type: String, required: true, index: { unique: true } },
	ownerId: { type: String, required: true },
    // Type translates to 'any'
	messages: { type: Schema.Types.Mixed },
});

export const TextChannel: Model<TextChannelInterface> = mongoose.model('TextChannels', TextChannelSchema);