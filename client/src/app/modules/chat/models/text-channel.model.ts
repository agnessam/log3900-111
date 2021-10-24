import { Message } from './message.model';

export interface TextChannel {
    channelId: string;
    channelName: string;
    ownerId: string;
    messages?: Message[];
}