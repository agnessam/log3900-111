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
  httpPut,
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

  @httpGet('/:channelId')
  public async getChannelById(@request() req: Request) {
    return await this.textChannelRepository.findById(req.params.id);
  }

  @httpGet('/')
  public async createChannel(@request() req: Request) {
    return await this.textChannelRepository.create(req.body);
  }

  @httpDelete('/:channelId')
  public async deleteChannel(@request() req: Request) {
    return await this.textChannelRepository.deleteById(req.params.id);
  }

  @httpPut('/:channelId')
  public async updateChannel(@request() req: Request) {
    return await this.textChannelRepository.updateById(req.params.id, req.body);
  }
  // handle quand tout le monde part de la convo il faut save les messages
  // par defaut seuls les messages envoyes deuis sa connexion sont affiches
  // socket array save at certain length
}