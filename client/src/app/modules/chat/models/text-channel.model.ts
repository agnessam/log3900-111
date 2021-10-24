import { Message } from './message.model';

export interface TextChannel {
    channelName: string;
    owner: string;
    messages: Message[];
}