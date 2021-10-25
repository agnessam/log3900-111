import { Request, Response } from 'express';
import { injectable } from 'inversify';
import { request, response } from 'inversify-express-utils';
import { User, UserInterface } from '../../../domain/models/user';
import { GenericRepository } from './generic_repository';

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

  public async getUserDrawings(userId: string) {
    return new Promise((resolve, reject) => {
      User.findById({_id: userId}).populate('drawings').exec((err, user) => {
        if (err || !user) { reject(err); }
        resolve(user!.drawings);
      });
    });
  }
}
