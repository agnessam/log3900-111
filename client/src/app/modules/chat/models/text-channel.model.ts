import { Message } from './message.model';

export interface TextChannel {
    _id: string;
    name: string;
    ownerId: string;
    messages?: Message[];
}