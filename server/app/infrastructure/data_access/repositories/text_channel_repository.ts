import { TextChannel, TextChannelInterface } from '../../../domain/models/TextChannel';
import { injectable } from 'inversify';
import { GenericRepository } from './generic_repository';

@injectable()
export class TextChannelRepository extends GenericRepository<TextChannelInterface> {
	constructor() {
		super(TextChannel);
	}
}