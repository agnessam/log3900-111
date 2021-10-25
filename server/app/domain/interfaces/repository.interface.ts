import { UserInterface } from '../models/user';
import { TextChannelInterface } from '../models/TextChannel';
import { TeamInterface } from '../models/teams';
import { DrawingInterface } from '../models/Drawing';

export interface Repository<T> {
  save(doc: T): Promise<T>;
  create(model: T): Promise<T>;
  findAll(): Promise<T[]>;
  findById(id: string): Promise<T>;
  updateById(id: string, model: T): Promise<T>;
  deleteById(id: string): Promise<T>;
}

export type UserRepositoryInterface = Repository<UserInterface>;
export type TeamRepositoryInterface = Repository<TeamInterface>;
export type DrawingRepositoryInterface = Repository<DrawingInterface>;
export type TextChannelRepositoryInterface = Repository<TextChannelInterface>;
