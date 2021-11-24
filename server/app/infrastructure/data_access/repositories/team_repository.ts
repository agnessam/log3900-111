import { Drawing } from '../../../domain/models/Drawing';
import { Post } from '../../../domain/models/Post';
import { injectable } from 'inversify';
import { Team, TeamInterface } from '../../../domain/models/teams';
import { User, UserInterface } from '../../../domain/models/user';
import { GenericRepository } from './generic_repository';

@injectable()
export class TeamRepository extends GenericRepository<TeamInterface> {
  constructor() {
    super(Team);
  }

  public async createTeam(team: TeamInterface): Promise<TeamInterface> {
    return new Promise<TeamInterface>((resolve, reject) => {
      const newTeam = new Team({
        name: team.name,
        description: team.description,
        owner: team.owner,
      });
      newTeam
        .save()
        .then((createdTeam: TeamInterface) => {
          (createdTeam.members as string[]).push(createdTeam.owner);
          createdTeam.save();

          User.findById(
            { _id: createdTeam.owner },
            (err: Error, user: UserInterface) => {
              if (err) {
                reject(err);
              }

              user.teams.push(createdTeam._id);
              user.save();
            },
          );

          resolve(createdTeam);
        })
        .catch((err) => {
          reject(err);
        });
    });
  }

  // For custom request

  public async deleteTeam(teamId: string) {
    return new Promise((resolve, reject) => {
      Team.findOneAndDelete(
        { _id: teamId },
        (err: Error, deletedTeam: TeamInterface) => {
          if (err || !deletedTeam) {
            reject(err);
          }
          console.log(deletedTeam);
          Drawing.deleteMany({ _id: { $in: deletedTeam.drawings } });
          Post.deleteMany({ _id: { $in: deletedTeam.posts } });
          resolve(deletedTeam);
        },
      );
    });
  }

  public async getTeamMembers(teamId: string) {
    return new Promise((resolve, reject) => {
      Team.findById({ _id: teamId })
        .populate(['members'])
        .exec((err, team) => {
          if (err || !team) {
            reject(err);
          }
          const allTeamMembers = team!.members;
          resolve(allTeamMembers);
        });
    });
  }

  public async addMemberToTeam(teamId: string, userId: string) {
    return new Promise((resolve, reject) => {
      Team.findById({ _id: teamId }, (err: Error, team: TeamInterface) => {
        if (err || !team) {
          reject(err);
        }

        (team.members as string[]).push(userId);
        team.save().then((team) => {
          User.findById({ _id: userId }, (err: Error, user: UserInterface) => {
            if (err || !user) {
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

  public async getPosts(teamId: string) {
    return new Promise((resolve, reject) => {
      Team.findById({ _id: teamId })
        .populate('posts')
        .exec((err, team) => {
          if (err || !team) {
            reject(err);
          }
          resolve(team!.posts);
        });
    });
  }
}
