import { TYPES } from '@app/domain/constants/types';
import { TextChannelRepository } from '@app/infrastructure/data_access/repositories/textchannel_repository';
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

@controller('/channels', passport.authenticate('jwt', { session: false }))
export class UserController {
  @inject(TYPES.TextChannelRepository) public textChannelRepository: TextChannelRepository;

  @httpGet('/')
  public async get() {
    return await this.textChannelRepository.findAll();
  }
}