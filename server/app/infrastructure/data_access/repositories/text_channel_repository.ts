import { TextChannel, TextChannelInterface } from '../../../domain/models/TextChannel';
import { injectable } from 'inversify';
import { GenericRepository } from './generic_repository';
import { Message } from '@app/domain/models/Message';

@injectable()
export class TextChannelRepository extends GenericRepository<TextChannelInterface> {
	constructor() {
		super(TextChannel);
	}

	public async getMessages(channelId: string) {
		// not sure if necessary to find textchannel first since there's no message array
		return new Promise((resolve, reject) => {
		  TextChannel.findById({ _id: channelId }, (err: Error) => {
			if (err) {
				reject(err);
			}
			Message.find({ textChannel: channelId }, (err: Error) => {
				if (err) {
					reject(err);
				}
			})
			.exec((messages) => {
				resolve(messages);
			})

			
		  })	
		}
	)};
}