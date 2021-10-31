import { Message, MessageInterface } from '../../../domain/models/Message';
import { injectable } from 'inversify';
import { GenericRepository } from './generic_repository';

@injectable()
export class MessageRepository extends GenericRepository<MessageInterface> {
	constructor() {
		super(Message);
	}

  // public async getMessages(channelId: string) {
  //     return new Promise((resolve, reject) => {
  //       // Message.find({textChannel: channelId})
  //       // .populate()
  //       // TextChannel.findById({ _id: channelId })
  //       // .populate

  //   });
  }
}