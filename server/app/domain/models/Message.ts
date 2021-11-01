import mongoose, { Document, Model } from 'mongoose';

export interface MessageInterface extends Document{
    message: string, 
    timestamp: string, 
    author: string,
    roomName: string
}

const MessageSchema = new mongoose.Schema({
	message: { type: String, required: true },
    timestamp: { type: String, required: true },
	author: { type: String, required: true },
    roomName: { type: String, required: true, ref: 'TextChannel' },
});

export const Message: Model<MessageInterface> = mongoose.model('Messages', MessageSchema);