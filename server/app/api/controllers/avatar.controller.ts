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
import { upload } from '../middleware/upload_middleware';

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

  @httpPost('/upload', upload.single('avatar'))
  public async uploadAvatar(@request() req: Request) {
    if (req.file) {
      console.log(req.file);
      // return await this.avatarRepository.create(req.file.location);
    }
  }

  @httpPost('/:id')
  public async deleteAvatar(@request() req: Request) {
    return await this.avatarRepository.deleteById(req.params.id);
  }

  @httpGet('/default')
  public async getDefaultAvatars() {
    return await this.avatarRepository.findManyByQuery({
      default: true,
    });
  }
}
