import { Message, MessageInterface } from '../../../domain/models/Message';
import { injectable } from 'inversify';
import { GenericRepository } from './generic_repository';

@injectable()
export class MessageRepository extends GenericRepository<MessageInterface> {
	constructor() {
		super(Message);
	}

    public async getMessagesByChannelId(channelId: string) {
        return new Promise((resolve, reject) => {
        //   TODO: find all messages from channel 
        // should this be done in channel repository?

      });
    }
}