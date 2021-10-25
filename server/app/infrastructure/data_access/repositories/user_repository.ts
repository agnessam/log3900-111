import { injectable } from 'inversify';
import { User, UserInterface } from '../../../domain/models/user';
import { GenericRepository } from './generic_repository';
import { Request, Response } from 'express';
import { request, response } from 'inversify-express-utils';

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

  public async getDrawings(userId: string) {
    try {
      User.findOne({_id: userId}).populate('drawings').exec(
        (err: Error, drawings) => {
          if (err) {
            return err;
          }
          return drawings;
        }
      );
    } catch (err) {
      return err;
    }
  }
  
}
