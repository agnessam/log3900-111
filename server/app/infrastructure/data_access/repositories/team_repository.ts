import { injectable } from 'inversify';
import { Team, TeamInterface } from '../../../domain/models/teams';
import { User, UserInterface } from '../../../domain/models/user';
import { GenericRepository } from './generic_repository';

@injectable()
export class TeamRepository extends GenericRepository<TeamInterface> {
  constructor() {
    super(Team);
  }

  public async create(team: TeamInterface): Promise<TeamInterface> {
    return new Promise<TeamInterface>((resolve, reject) => {
      Team.create(team, (err: Error, team: TeamInterface) => {
        if (err) {
          reject(err);
        }
        User.findById(
          { _id: team.owner },
          (err: Error, user: UserInterface) => {
            if (err) {
              reject(err);
            }

            user.teams.push(team._id);
            user.save();
          },
        );

        resolve(team);
      });
    });
  }

  // For custom request

  public async getTeamMembers(teamId: string) {
    return new Promise((resolve, reject) => {
      Team.findById({ _id: teamId })
        .populate(['members', 'owner'])
        .exec((err, team) => {
          if (err || !team) {
            reject(err);
          }
          const allTeamMembers = team!.members.concat(team!.owner);
          resolve(allTeamMembers);
        });
    });
  }

  public async addMemberToTeam(teamId: string, userId: string) {
    return new Promise((resolve, reject) => {
      Team.findById({ _id: teamId }, (err: Error, team: TeamInterface) => {
        if (err) {
          reject(err);
        }

        team.members.push(userId);
        team.save().then((team) => {
          User.findById({ _id: userId }, (err: Error, user: UserInterface) => {
            if (err) {
              reject(err);
            }

            user.teams.push(team._id);
            user.save();
          });
          resolve(team);
        });
      });
    });
  }

  public async removeMemberFromTeam(teamId: string, userId: string) {
    return new Promise((resolve, reject) => {
      Team.findByIdAndUpdate(
        { _id: teamId },
        { $pull: { members: { $in: userId } } },
        (err: Error, team: TeamInterface) => {
          if (err) {
            reject(err);
          }
          User.findByIdAndUpdate(
            { _id: userId },
            { $pull: { teams: { $in: teamId } } },
            (err: Error, user: UserInterface) => {
              if (err) {
                reject(err);
              }
            },
          );
          resolve(team);
        },
      );
    });
  }

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
