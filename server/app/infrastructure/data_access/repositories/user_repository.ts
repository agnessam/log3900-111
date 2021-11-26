import { Request, Response } from 'express';
import { injectable } from 'inversify';
import { request, response } from 'inversify-express-utils';
import { User, UserInterface } from '../../../domain/models/user';
import { GenericRepository } from './generic_repository';
import { Team } from '../../../domain/models/teams';
import bcrypt from 'bcrypt';

declare global {
  namespace Express {
    interface User {
      id: string;
      user: string;
    }
  }
}

@injectable()
export class UserRepository extends GenericRepository<UserInterface> {
  constructor() {
    super(User);
  }

  public async getMe(@request() req: Request, @response() res: Response) {
    try {
      const userId = req.user?.id as string;
      const user = await this.findById(userId);
      res.json({
        user,
        err: null,
      });
    } catch (err) {
      res.json({
        user: null,
        err,
      });
    }
  }

  public async changePassword(
    userId: string,
    currentPassword: string,
    newPassword: string,
  ) {
    return new Promise(async (resolve, reject) => {
      const currentUser = await User.findOne({ _id: userId });
      const validate = await currentUser!.isValidPassword(currentPassword);
      if (!validate) {
        resolve({ err: 'Incorrect password' });
      }

      const hashedPassword = await bcrypt.hash(newPassword, 10);

      currentUser!.password = hashedPassword;
      currentUser!.save();
      resolve(currentUser);
    });
  }

  public async getUserTeams(userId: string) {
    return new Promise((resolve, reject) => {
      Team.find({ members: userId }).exec((err, teams) => {
        if (err || !teams) {
          reject(err);
        }
        const userTeams = teams;
        resolve(userTeams);
      });
    });
  }

  public async getUserDrawings(userId: string) {
    return new Promise((resolve, reject) => {
      User.findById({ _id: userId })
        .populate('drawings')
        .exec((err, user) => {
          if (err || !user) {
            reject(err);
          }
          resolve(user!.drawings);
        });
    });
  }

  public async getPosts(userId: string) {
    return new Promise((resolve, reject) => {
      User.findById({ _id: userId })
        .populate('posts')
        .exec((err, user) => {
          if (err || !user) {
            reject(err);
          }
          resolve(user!.posts);
        });
    });
  }

  public async followUser(followed: string, followedBy: string) {
    return new Promise((resolve, reject) => {
      User.findById(
        { _id: followed },
        (err: Error, followedUser: UserInterface) => {
          if (err || !followedUser) {
            reject(err);
          }
          (followedUser.followers as string[]).push(followedBy);
          followedUser.save();
          User.findById(
            { _id: followedBy },
            (err: Error, followedByUser: UserInterface) => {
              if (err || !followedByUser) {
                reject(err);
              }
              (followedByUser.following as string[]).push(followed);
              followedByUser.save();
            },
          );
          resolve(followedUser);
        },
      );
    });
  }

  public async unfollowUser(unfollowedId: string, unfollowedById: string) {
    return new Promise((resolve, reject) => {
      User.findByIdAndUpdate(
        { _id: unfollowedById },
        { $pull: { following: unfollowedId } },
        (err: Error, unfollowedByUser: UserInterface) => {
          if (err || !unfollowedByUser) {
            reject(err);
          }
        },
      );
      User.findByIdAndUpdate(
        { _id: unfollowedId },
        { $pull: { followers: unfollowedById } },
        { new: true },
        (err: Error, unfollowedUser: UserInterface) => {
          if (err || !unfollowedUser) {
            reject(err);
          }
          resolve(unfollowedUser);
        },
      );
    });
  }

  public async updateLogout(userId: string) {
    return new Promise((resolve, reject) => {
      User.findOneAndUpdate(
        { _id: userId },
        { lastLogout: new Date() },
        (err: Error, user: any) => {
          if (err) {
            reject(err);
          }
          resolve(true);
        },
      );
    });
  }
}
