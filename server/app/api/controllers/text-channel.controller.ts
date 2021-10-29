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

  @httpGet('/:id')
  public async getChannelById(@request() req: Request) {
    return await this.textChannelRepository.findById(req.params.id);
  }

  @httpPost('/')
  public async createChannel(@request() req: Request) {
    return await this.textChannelRepository.create(req.body);
  }

  @httpDelete('/:id')
  public async deleteChannel(@request() req: Request) {
    return await this.textChannelRepository.deleteById(req.params.id);
  }

  @httpPatch('/:id')
  public async updateChannel(@request() req: Request) {
    return await this.textChannelRepository.updateById(req.params.id, req.body);
  }
}