import { Post, PostInterface } from '../../../domain/models/Post';
import { injectable } from 'inversify';
import { GenericRepository } from './generic_repository';
import { Comment, CommentInterface } from '../../../domain/models/Comment';

@injectable()
export class PostRepository extends GenericRepository<PostInterface> {
  constructor() {
    super(Post);
  }

  public async getAllPopulatedPosts() {
    return new Promise((resolve, reject) => {
      Post.find({})
        .populate('comments')
        .exec((err, posts) => {
          if (err || !posts) {
            reject(err);
          }
          resolve(posts);
        });
    });
  }

  public async getPopulatedPostById(postId: string) {
    return new Promise((resolve, reject) => {
      Post.findById({ _id: postId })
        .populate({ path: 'comments', populate: { path: 'authorId' } })
        .exec((err, post) => {
          if (err || !post) {
            reject(err);
          }
          resolve(post);
        });
    });
  }

  public async addComment(
    userId: string,
    postId: string,
    comment: CommentInterface,
  ): Promise<CommentInterface> {
    return new Promise<CommentInterface>((resolve, reject) => {
      const newComment = new Comment({
        content: comment.content,
        authorId: userId,
        postId: postId,
      });
      newComment.save().then((savedComment) => {
        Post.findById({ _id: postId }, (err: Error, post: PostInterface) => {
          if (err || !post) {
            reject(err);
          }
          post.comments.push(savedComment._id);
          post.save();
        });
      });
      resolve(newComment);
    });
  }
}
