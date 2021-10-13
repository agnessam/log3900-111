import { injectable } from 'inversify';
import { User, UserInterface } from '../../../domain/models/user';
import { GenericRepository } from './generic_repository';

@injectable()
export class UserRepository extends GenericRepository<UserInterface> {
	constructor() {
		super(User);
	}
}
