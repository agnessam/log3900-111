import { TYPES } from '../../domain/constants/types';
import { inject } from 'inversify';
import {
  controller,
  httpGet,
  httpPost,
  request,
} from 'inversify-express-utils';
import { AvatarRepository } from '../../infrastructure/data_access/repositories/avatar_repository';
import { Request } from 'express';

@controller('/avatars')
export class AvatarController {
  @inject(TYPES.AvatarRepository) private avatarRepository: AvatarRepository;

  @httpGet('/')
  public async get() {
    return await this.avatarRepository.findAll();
  }

  @httpPost('/')
  public async postAvatar(@request() req: Request) {
    return await this.avatarRepository.create(req.body);
  }
}
