import { ChatParticipant } from 'src/app/model/chat-participant.model';
import { Message } from 'src/app/services/socket/message.model';

export class ChatWindow
{
    channelName: string;
    owner: ChatParticipant;
    messages: Message[];
    // hasFocus: boolean;
    isCollapsed: boolean;

    constructor(channelName: string, participant: ChatParticipant, isCollapsed: boolean) {
        this.messages = [];
        this.channelName = channelName;
        this.owner = participant;
        this.isCollapsed = isCollapsed;
    }

}