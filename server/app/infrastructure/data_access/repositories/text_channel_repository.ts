import { TextChannel, TextChannelInterface } from '../../../domain/models/TextChannel';
import { injectable } from 'inversify';
import { GenericRepository } from './generic_repository';
import { Message, MessageInterface } from '@app/domain/models/Message';

@injectable()
export class TextChannelRepository extends GenericRepository<TextChannelInterface> {
	constructor() {
		super(TextChannel);
	}

	public async getMessages(channelName: string): Promise<MessageInterface[]> {
		return new Promise((resolve, reject) => {
			Message.find({ roomName: channelName })
			.exec((err, messages) => {
				if (err) {
					reject(err);
				}
				resolve(messages);
			})
		})
	};

	public async getChannelsByName(channelName: string): Promise<TextChannelInterface[]> {
		return new Promise((resolve, reject) => {
			TextChannel.find({name: new RegExp('^'+ channelName +'$', "i")})
			.exec((err, channels) => {
				if (err) {
					reject(err);
				}
				resolve(channels);
			})
		})
	}
}