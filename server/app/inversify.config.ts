import { ContainerModule } from 'inversify';
import { TYPES } from './domain/constants/types';
import {
  DrawingRepositoryInterface,
  UserRepositoryInterface,
  TextChannelRepositoryInterface,
  MessageRepositoryInterface,
} from './domain/interfaces/repository.interface';
import { SocketServiceInterface } from './domain/interfaces/socket.interface';
import { ChatSocketService } from './domain/services/sockets/chat-socket.service';
import { TextChannelRepository } from './infrastructure/data_access/repositories/text_channel_repository';
import { UserRepository } from './infrastructure/data_access/repositories/user_repository';
import { TeamRepositoryInterface } from './domain/interfaces/repository.interface';
import { TeamRepository } from './infrastructure/data_access/repositories/team_repository';
import { DrawingRepository } from './infrastructure/data_access/repositories/drawing_repository';
import { MessageRepository } from './infrastructure/data_access/repositories/message_repository';
import { DrawingSocketService } from './domain/services/sockets/drawing-socket.service';
import { SearchService } from './domain/services/search.service';

export const referenceDataIoCModule = new ContainerModule((bind) => {
  // Services
  bind<SocketServiceInterface>(TYPES.ChatSocketService)
    .to(ChatSocketService)
    .inSingletonScope();

  bind<DrawingSocketService>(TYPES.DrawingSocketService)
    .to(DrawingSocketService)
    .inSingletonScope();

  bind<SearchService>(TYPES.SearchService).to(SearchService).inSingletonScope();

  // Repositories
  bind<UserRepositoryInterface>(TYPES.UserRepository)
    .to(UserRepository)
    .inSingletonScope();

  bind<TextChannelRepositoryInterface>(TYPES.TextChannelRepository)
    .to(TextChannelRepository)
    .inSingletonScope();

  bind<TeamRepositoryInterface>(TYPES.TeamRepository)
    .to(TeamRepository)
    .inSingletonScope();

  bind<DrawingRepositoryInterface>(TYPES.DrawingRepository)
    .to(DrawingRepository)
    .inSingletonScope();

  bind<MessageRepositoryInterface>(TYPES.MessageRepository)
    .to(MessageRepository)
    .inSingletonScope();
});
