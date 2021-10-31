import { TYPES } from '../../domain/constants/types';
import { TextChannelRepository } from '../../infrastructure/data_access/repositories/text_channel_repository';
import { Request } from 'express';
import { inject } from 'inversify';
import {
  controller,
  httpDelete,
  httpGet,
  httpPatch,
  httpPost,
  request,
} from 'inversify-express-utils';
import passport from 'passport';

@controller('/channels', passport.authenticate('jwt', { session: false }))
export class TextChannelController {
  @inject(TYPES.TextChannelRepository) public textChannelRepository: TextChannelRepository;

  @httpGet('/')
  public async get() {
    return await this.textChannelRepository.findAll();
  }

  @httpGet('/:channelId')
  public async getChannelById(@request() req: Request) {
    return await this.textChannelRepository.findById(req.params.channelId);
  }

  @httpPost('/')
  public async createChannel(@request() req: Request) {
    return await this.textChannelRepository.create(req.body);
  }

  @httpDelete('/:channelId')
  public async deleteChannel(@request() req: Request) {
    return await this.textChannelRepository.deleteById(req.params.channelId);
  }

  @httpPatch('/:channelId')
  public async updateChannel(@request() req: Request) {
    return await this.textChannelRepository.updateById(req.params.channelId, req.body);
  }

  @httpGet('/:channelId/messages')
  public async getMessages(@request() req: Request) {
    return await this.textChannelRepository.getMessages(req.params.channelId);
  }
}