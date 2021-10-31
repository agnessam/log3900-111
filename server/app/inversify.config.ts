import { ContainerModule } from 'inversify';
import { TYPES } from './domain/constants/types';
import { UserRepositoryInterface } from './domain/interfaces/repository.interface';
import { SocketServiceInterface } from './domain/interfaces/socket.interface';
import { ChatSocketService } from './domain/services/sockets/chat-socket.service';
import { DrawingSocketService } from './domain/services/sockets/drawing-socket.service';
import { UserRepository } from './infrastructure/data_access/repositories/user_repository';

export const referenceDataIoCModule = new ContainerModule((bind) => {
  // Services
  bind<SocketServiceInterface>(TYPES.ChatSocketService)
    .to(ChatSocketService)
    .inSingletonScope();

  bind<DrawingSocketService>(TYPES.DrawingSocketService)
    .to(DrawingSocketService)
    .inSingletonScope();

  // Repositories
  bind<UserRepositoryInterface>(TYPES.UserRepository)
    .to(UserRepository)
    .inSingletonScope();
});
