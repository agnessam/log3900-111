import { TYPES } from '../../domain/constants/types';
import { PostRepository } from '../../infrastructure/data_access/repositories/post_repository';
import { inject } from 'inversify';
import {
  controller,
  httpGet,
  httpPost,
  request,
} from 'inversify-express-utils';
import { Request } from 'express';

@controller('/posts')
export class PostController {
  @inject(TYPES.PostRepository) public postRepository: PostRepository;

  @httpGet('/')
  public async getPosts() {
    return this.postRepository.getAllPopulatedPosts();
  }

  @httpGet('/:id')
  public async getPostById(@request() req: Request) {
    return this.postRepository.getPopulatedPostById(req.params.id);
  }

  @httpPost('/:id/comments')
  public async addComment(@request() req: Request) {
    return this.postRepository.addComment(
      req.user!.id,
      req.params.id,
      req.body,
    );
  }
}
