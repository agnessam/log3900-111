import { Message, MessageInterface } from '../../../domain/models/Message';
import { injectable } from 'inversify';
import { GenericRepository } from './generic_repository';

@injectable()
export class MessageRepository extends GenericRepository<MessageInterface> {
	constructor() {
		super(Message);
	}

	public async storeMessages(messages: MessageInterface[]): Promise<void> {
		return new Promise((resolve, reject) => {
			Message.insertMany(messages, { ordered: false }, (err: Error) => {
				if (err) {
				  reject(err);
				}
				resolve();
			});
		})
	}
}