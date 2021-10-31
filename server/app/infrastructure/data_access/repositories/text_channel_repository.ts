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
		return new Promise((resolve, reject) => {
			Message.find({ textChannel: channelId })
			.exec((err, messages) => {
				if (err) {
					reject(err);
				}
				resolve(messages);
			})
		})
	};

	public async getChannelsByName(channelName: string) {
		return new Promise((resolve, reject) => {
			TextChannel.find({name: channelName})
			.exec((err, channels) => {
				if (err) {
					reject(err);
				}
				resolve(channels);
			})
		})
	}
}