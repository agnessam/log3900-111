import { injectable } from 'inversify';
import { Team, TeamInterface } from '../../../domain/models/teams';
import { GenericRepository } from './generic_repository';

@injectable()
export class TeamRepository extends GenericRepository<TeamInterface> {
  constructor() {
    super(Team);
  }

  // For custom request

  public async getTeamDrawings(teamId: string) {
    return new Promise((resolve, reject) => {
      Team.findById({ _id: teamId })
        .populate('drawings')
        .exec((err, team) => {
          if (err || !team) {
            reject(err);
          }
          resolve(team!.drawings);
        });
    });
  }
}
