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
        .populate({ path: 'comments', populate: { path: 'authorId' } })
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
  ): Promise<PostInterface> {
    return new Promise<PostInterface>((resolve, reject) => {
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
          resolve(post);
        });
      });
    });
  }

  public async addLike(userId: string, postId: string) {
    return new Promise<PostInterface>((resolve, reject) => {
      Post.findByIdAndUpdate(
        { _id: postId },
        { $push: { likes: userId } },
        { new: true },
        (err: Error, post: PostInterface) => {
          if (err || !post) {
            reject(err);
          }
          resolve(post);
        },
      );
    });
  }

  public async removeLike(userId: string, postId: string) {
    return new Promise<PostInterface>((resolve, reject) => {
      Post.findByIdAndUpdate(
        { _id: postId },
        { $pull: { likes: userId } },
        { new: true },
        (err: Error, post: PostInterface) => {
          if (err || !post) {
            reject(err);
          }
          resolve(post);
        },
      );
    });
  }
}
