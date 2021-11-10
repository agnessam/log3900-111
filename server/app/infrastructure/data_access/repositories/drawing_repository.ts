import {
  PublishedDrawing,
  PublishedDrawingInterface,
} from '../../../domain/models/PublishedDrawing';
import { User, UserInterface } from '../../../domain/models/user';
import { injectable } from 'inversify';
import { Types } from 'mongoose';
import { Drawing, DrawingInterface } from '../../../domain/models/Drawing';
import { Team, TeamInterface } from '../../../domain/models/teams';
import { User, UserInterface } from '../../../domain/models/User';
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
        _id: new Types.ObjectId(),
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
        _id: new Types.ObjectId(),
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
  ): Promise<PublishedDrawingInterface> {
    return new Promise<PublishedDrawingInterface>((resolve, reject) => {
      const publishedDrawing = new PublishedDrawing({
        _id: new Types.ObjectId(),
        dataUri: drawing.dataUri,
        ownerId: drawing.ownerId,
        ownerModel: drawing.ownerModel,
        name: drawing.name,
      });
      publishedDrawing.save().then((publishedDrawing) => {
        if (publishedDrawing.ownerModel == 'Team') {
          Team.findById(
            { _id: publishedDrawing.ownerId },
            (err: Error, team: TeamInterface) => {
              if (err || !team) {
                reject(err);
              }
              team.publishedDrawings.push(publishedDrawing._id);
              team.save();
            },
          );
        } else {
          User.findById(
            { _id: publishedDrawing.ownerId },
            (err: Error, user: UserInterface) => {
              if (err || !user) {
                reject(err);
              }
              user.publishedDrawings.push(publishedDrawing._id);
              user.save();
            },
          );
        }
      });
      resolve(publishedDrawing);
    });
  }
}
