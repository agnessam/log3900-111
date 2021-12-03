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
  queryParam,
  request,
} from 'inversify-express-utils';
import passport from 'passport';
import { ChatSocketService } from '@app/domain/services/sockets/chat-socket.service';

@controller('/channels', passport.authenticate('jwt', { session: false }))
export class TextChannelController {
  @inject(TYPES.TextChannelRepository)
  private textChannelRepository: TextChannelRepository;
  @inject(TYPES.ChatSocketService) private chatSocketService: ChatSocketService;

  @httpGet('/')
  public async get() {
    return await this.textChannelRepository.getPublicChannels();
  }

  @httpGet('/teams')
  public async getChannelsByTeamId() {
    return await this.textChannelRepository.getTeamChannels();
  }

  @httpGet('/:channelId')
  public async getChannelById(@request() req: Request) {
    return await this.textChannelRepository.findById(req.params.channelId);
  }

  @httpGet('/drawings/:drawingId')
  public async getChannelByDrawingId(@request() req: Request) {
    return await this.textChannelRepository.getChannelByDrawingId(
      req.params.drawingId,
    );
  }

  @httpPost('/')
  public async createChannel(@request() req: Request) {
    return await this.textChannelRepository.create(req.body);
  }

  @httpDelete('/:channelId')
  public async deleteChannel(@request() req: Request) {
    try {
      const deletedChannel = await this.textChannelRepository.deleteById(
        req.params.channelId,
      );
      this.chatSocketService.emitLeave(deletedChannel.name);
      this.textChannelRepository.deleteMessages(req.params.id);
      return deletedChannel;
    } catch (e: any) {
      console.log("Couldn't delete channel: ", e);
      return e;
    }
  }

  @httpDelete('/:channelId/messages')
  public async deleteMessages(@request() req: Request) {
    return await this.textChannelRepository.deleteMessages(
      req.params.channelId,
    );
  }

  @httpPatch('/:channelId')
  public async updateChannel(@request() req: Request) {
    return await this.textChannelRepository.updateById(
      req.params.channelId,
      req.body,
    );
  }

  @httpGet('/:channelId/messages')
  public async getMessages(@request() req: Request) {
    return await this.textChannelRepository.getMessages(req.params.channelId);
  }

  @httpGet('/all/search')
  public async searchChannels(@queryParam('q') query: string) {
    const channels = await this.textChannelRepository.findManyByQuery({
      $or: [{ name: { $regex: new RegExp(query, 'ig') }, isPrivate: false }],
    });
    return [...channels];
  }
}
