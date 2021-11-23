import { Post, PostInterface } from '../../../domain/models/Post';
import { injectable } from 'inversify';
import { Types } from 'mongoose';
import { Drawing, DrawingInterface } from '../../../domain/models/Drawing';
import { Team, TeamInterface } from '../../../domain/models/teams';
import { User, UserInterface } from '../../../domain/models/user';
import { GenericRepository } from './generic_repository';

@injectable()
export class DrawingRepository extends GenericRepository<DrawingInterface> {
  constructor() {
    super(Drawing);
  }

  public async createUserDrawing(
    item: DrawingInterface,
    ownerId: string,
  ): Promise<DrawingInterface> {
    return new Promise<DrawingInterface>((resolve, reject) => {
      const drawing = new Drawing({
        dataUri: item.dataUri,
        ownerId: ownerId,
        ownerModel: 'User',
        name: item.name,
      });
      drawing.save().then((drawing) => {
        User.findById(
          { _id: drawing.ownerId },
          (err: Error, user: UserInterface) => {
            if (err) {
              reject(err);
            }
            user.drawings.push(drawing._id);
            user.save();
          },
        );
      });
      resolve(drawing);
    });
  }

  public async createTeamDrawing(
    item: DrawingInterface,
  ): Promise<DrawingInterface> {
    return new Promise<DrawingInterface>((resolve, reject) => {
      const drawing = new Drawing({
        dataUri: item.dataUri,
        ownerId: item.ownerId,
        ownerModel: 'Team',
        name: item.name,
      });
      drawing.save().then((drawing) => {
        Team.findById(
          { _id: drawing.ownerId },
          (err: Error, team: TeamInterface) => {
            if (err) {
              reject(err);
            }
            team.drawings.push(drawing._id);
            team.save();
          },
        );
      });
      resolve(drawing);
    });
  }

  public async publishDrawing(
    drawing: DrawingInterface,
  ): Promise<PostInterface> {
    return new Promise<PostInterface>((resolve, reject) => {
      const post = new Post({
        _id: new Types.ObjectId(),
        dataUri: drawing.dataUri,
        owner: drawing.ownerId,
        ownerModel: drawing.ownerModel,
        name: drawing.name,
      });
      post.save().then((createdPost) => {
        if (createdPost.ownerModel == 'Team') {
          Team.findById(
            { _id: createdPost.owner },
            (err: Error, team: TeamInterface) => {
              if (err || !team) {
                reject(err);
              }
              team.posts.push(createdPost._id);
              team.save();
            },
          );
        } else {
          User.findById(
            { _id: createdPost.owner },
            (err: Error, user: UserInterface) => {
              if (err || !user) {
                reject(err);
              }
              user.posts.push(createdPost._id);
              user.save();
            },
          );
        }
      });
      resolve(post);
    });
  }
}
