import { TYPES } from '../../domain/constants/types';
import { UserRepository } from '../../infrastructure/data_access/repositories/user_repository';
import { Request, Response } from 'express';
import { inject } from 'inversify';
import {
  controller,
  httpDelete,
  httpGet,
  httpPatch,
  httpPost,
  request,
  response,
} from 'inversify-express-utils';
import passport from 'passport';
import { upload } from '../middleware/upload_middleware';

@controller('/users', passport.authenticate('jwt', { session: false }))
export class UserController {
  @inject(TYPES.UserRepository) public userRepository: UserRepository;

  @httpGet('/')
  public async get() {
    return await this.userRepository.findAll();
  }

  @httpGet('/me')
  public async getMe(@request() req: Request, @response() res: Response) {
    return await this.userRepository.getMe(req, res);
  }

  @httpGet('/:id')
  public async getUserById(@request() req: Request) {
    return await this.userRepository.findById(req.params.id);
  }

  @httpPost('/')
  public async createUser(@request() req: Request) {
    return await this.userRepository.create(req.body);
  }

  @httpPatch('/:id')
  public async updateUser(@request() req: Request) {
    return await this.userRepository.updateById(req.params.id, req.body);
  }

  @httpDelete('/:id')
  public async deleteUser(@request() req: Request) {
    return await this.userRepository.deleteById(req.params.id);
  }

  @httpPost('/:id/avatar', upload.single('avatar'))
  public async setAvatar(@request() req: Request) {
    console.log(req);
  }

  @httpGet('/:id/drawings')
  public async getDrawings(@request() req: Request) {
    return await this.userRepository.getUserDrawings(req.params.id);
  }

  @httpGet('/:id/publishedDrawings')
  public async getPublishedDrawings(@request() req: Request) {
    return await this.userRepository.getPublishedDrawings(req.body);
  }

  @httpGet('/:id/teams')
  public async getTeams(@request() req: Request) {
    return await this.userRepository.getUserTeams(req.params.id);
  }
}
