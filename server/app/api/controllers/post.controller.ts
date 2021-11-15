import { TYPES } from '../../domain/constants/types';
import { PostRepository } from '../../infrastructure/data_access/repositories/post_repository';
import { inject } from 'inversify';
import { controller } from 'inversify-express-utils';

@controller('/posts')
export class PostController {
  @inject(TYPES.PostRepository) public postRepository: PostRepository;
}
