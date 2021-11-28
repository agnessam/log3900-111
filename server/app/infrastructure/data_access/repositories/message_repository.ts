import { Message, MessageInterface } from '../../../domain/models/Message';
import { injectable } from 'inversify';
import { GenericRepository } from './generic_repository';

@injectable()
export class MessageRepository extends GenericRepository<MessageInterface> {
	constructor() {
		super(Message);
	}

	public async storeMessages(messages: MessageInterface): Promise<void> {
		console.log("STORED MESSAGE" + messages.message)
		return new Promise((resolve, reject) => {
			Message.create(messages, (err: Error) => {
				if (err) {
					console.log("erreurrrr", err)
				  reject(err);
				}
				resolve();
			});
		})
	}
}