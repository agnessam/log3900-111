import { injectable } from 'inversify';
import { Message, MessageInterface } from '../../../domain/models/Message';
import {
  TextChannel,
  TextChannelInterface
} from '../../../domain/models/TextChannel';
import { GenericRepository } from './generic_repository';

@injectable()
export class TextChannelRepository extends GenericRepository<TextChannelInterface> {
  constructor() {
    super(TextChannel);
  }

  getPublicChannels() {
    return new Promise((resolve, reject) => {
      TextChannel.find({ isPrivate: false }, (err, channels) => {
        if (err) reject(err);
        resolve(channels);
      });
    });
  }

  public async getMessages(channelId: string): Promise<MessageInterface[]> {
    return new Promise((resolve, reject) => {
      Message.find({ roomId: channelId }).exec((err, messages) => {
        if (err) {
          reject(err);
        }
        resolve(messages);
      });
    });
  }

  public async deleteMessages(channelId: string): Promise<void> {
    return new Promise((resolve, reject) => {
      Message.deleteMany({ roomId: channelId }).exec((err) => {
        if (err) {
          reject(err);
        }
        resolve();
      });
    });
  }

  // Channels have unique names
  public async getChannelByName(
    channelName: string,
  ): Promise<TextChannelInterface> {
    return new Promise((resolve, reject) => {
      TextChannel.findOne({
        name: new RegExp('^' + channelName + '$', 'i'),
      }).exec((err, channel) => {
        if (err || !channel) {
          reject(err);
        }
        resolve(channel!);
      });
    });
  }

  public async getChannelByDrawingId(drawingId: string) {
    return new Promise((resolve, reject) => {
      TextChannel.findOne(
        { drawing: drawingId },
        (err: Error, channel: TextChannelInterface) => {
          if (err) reject(err);
          resolve(channel);
        },
      );
    });
  }

  public async getTeamChannels(ids: string[]) {
    return new Promise((resolve, reject) => {
      const query = { team: { $in: ids}};
      TextChannel.find(query as any, (err, channels) => {
        if (err) {
          reject(err);
        }
        resolve(channels);
      });
    });
  }
}
