import { Post, PostInterface } from '../../../domain/models/Post';
import { injectable } from 'inversify';
import { GenericRepository } from './generic_repository';

@injectable()
export class PostRepository extends GenericRepository<PostInterface> {
  constructor() {
    super(Post);
  }
}
