import { UserInterface } from '../models/user';

export interface Repository<T> {
	save(doc: T): Promise<T>;
	create(model: T): Promise<T>;
	findAll(): Promise<T[]>;
	findById(id: string): Promise<T>;
	updateById(id: string, model: T): Promise<T>;
	deleteById(id: string): Promise<T>;
}

export type UserRepositoryInterface = Repository<UserInterface>;
