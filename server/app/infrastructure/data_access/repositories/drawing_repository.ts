import { User, UserInterface } from '../../../domain/models/User';
import { injectable } from 'inversify';
import { Types } from 'mongoose';
import { Drawing, DrawingInterface } from '../../../domain/models/Drawing';
import { Team, TeamInterface } from '../../../domain/models/teams';
import { GenericRepository } from './generic_repository';

@injectable()
export class DrawingRepository extends GenericRepository<DrawingInterface> {
  constructor() {
    super(Drawing);
  }

  public async createUserDrawing(
    item: DrawingInterface,
  ): Promise<DrawingInterface> {
    return new Promise<DrawingInterface>((resolve, reject) => {
      const drawing = new Drawing({
        _id: new Types.ObjectId(),
        dataUri: item.dataUri,
        ownerId: item.ownerId,
        ownerModel: 'User',
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
}
